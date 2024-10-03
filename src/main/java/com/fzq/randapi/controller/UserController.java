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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "API for user registration, login, logout, and key management.")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * User register
     * @param userRegisterDTO user register request
     * @return Long
     */
    @Operation(summary = "User Registration", description = "Registers a new user with the given information.")
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.userRegister(userRegisterDTO);
    }

    @Operation(summary = "User Login", description = "Logs in a user with the given credentials.")
    @PostMapping("/login")
    public UserVO userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.userLogin(userLoginDTO, request);
    }

    @Operation(summary = "User Logout", description = "Logs out the current user.")
    @PostMapping("/logout")
    public Boolean userLogout(HttpServletRequest request) {
        return userService.userLogout(request);
    }

    @Operation(summary = "Get Current User", description = "Retrieves the currently logged-in user's details.")
    @GetMapping("/current")
    public UserVO getCurrentUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }

    @Operation(summary = "Generate Secret Key", description = "Generates a secret key for the given access key.")
    @PostMapping("/key")
    public KeyVO getSecretKey(@RequestBody KeyGenerateDTO keyGenerateDTO, HttpServletRequest request) {
        if (keyGenerateDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userService.getKey(keyGenerateDTO, request);
    }

}
