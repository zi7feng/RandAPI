package com.fzq.randapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fzq.randapi.common.ErrorCode;
import com.fzq.randapi.exception.BusinessException;
import com.fzq.randapi.mapper.UserMapper;
import com.fzq.randapi.model.entity.User;
import com.fzq.randapi.service.AuthService;
import com.fzq.randapi.service.RandService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RandServiceImpl implements RandService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthService authService;

    @Override
    @Transactional
    public int generateRandNumber(HttpServletRequest request) {
        // verify authentication
        authService.verifyRequest(request);

        // get accessKey and find the user
        String accessKey = request.getHeader("accessKey");
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("access_key", accessKey));
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "Invalid accessKey.");
        }

        // verify whether balance is enough
        if (user.getBalance() <= 0) {
            throw new BusinessException(ErrorCode.NO_AUTH, "No balance.");
        }

        // generate random number from 0 to 10
        int randomNumber = (int) (Math.random() * 11);

        // deduct 1 point from the user's balance
        user.setBalance(user.getBalance() - 1);
        userMapper.updateById(user);

        return randomNumber;
    }


}
