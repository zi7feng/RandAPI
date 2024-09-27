package com.fzq.randapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzq.randapi.common.ErrorCode;
import com.fzq.randapi.exception.BusinessException;
import com.fzq.randapi.mapper.UserMapper;
import com.fzq.randapi.model.entity.User;
import com.fzq.randapi.model.vo.UserVO;
import com.fzq.randapi.service.AuthService;
import com.fzq.randapi.utils.SignUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean verifyRequest(HttpServletRequest request) {

        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("access_key", accessKey));
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Invalid accessKey.");
        }

        // verify if nonce exists in redis
        Boolean isNonceUsed = redisTemplate.hasKey("nonce:" + nonce);
        if (isNonceUsed != null && isNonceUsed) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Nonce has been used.");
        }
        redisTemplate.opsForValue().set("nonce:" + nonce, "1", 5, TimeUnit.MINUTES);

        // Verify timestamp
        long currentTime = System.currentTimeMillis() / 1000;
        long requestTimestamp = Long.parseLong(timestamp);

        if (Math.abs(currentTime - requestTimestamp) > 300) { // 5 min
            throw new BusinessException(ErrorCode.NO_AUTH, "Request time is invalid or expired.");
        }

        // Generate server side signature
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", nonce);
        hashMap.put("body", body);
        hashMap.put("timestamp", timestamp);

        String serverSign = SignUtils.genSign(hashMap, user.getSecretKey());

        // Verify signature
        if(!sign.equals(serverSign)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "Signature verification failed.");
        }

        return true;

    }
}
