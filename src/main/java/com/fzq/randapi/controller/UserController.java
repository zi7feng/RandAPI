package com.fzq.randapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fzq.randapi.common.ErrorCode;
import com.fzq.randapi.exception.BusinessException;
import com.fzq.randapi.model.dto.user.KeyGenerateDTO;
import com.fzq.randapi.model.dto.user.UserRegisterDTO;
import com.fzq.randapi.model.dto.user.UserLoginDTO;
import com.fzq.randapi.model.vo.KeyVO;
import com.fzq.randapi.model.vo.UserVO;
import com.fzq.randapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * User register
     * @param userRegisterDTO user register request
     * @return Long
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.userRegister(userRegisterDTO);
    }

    @PostMapping("/login")
    public UserVO userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.userLogin(userLoginDTO, request);
    }

    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request) {
        return userService.userLogout(request);
    }

    @GetMapping("/current")
    public UserVO getCurrentUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }


    @PostMapping("/key")
    public KeyVO getSecretKey(@RequestBody KeyGenerateDTO keyGenerateDTO, HttpServletRequest request) {
        if (keyGenerateDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.getKey(keyGenerateDTO, request);
    }

}
