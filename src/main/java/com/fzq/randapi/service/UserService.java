package com.fzq.randapi.service;

import com.fzq.randapi.model.dto.user.KeyGenerateDTO;
import com.fzq.randapi.model.dto.user.UserLoginDTO;
import com.fzq.randapi.model.dto.user.UserRegisterDTO;
import com.fzq.randapi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fzq.randapi.model.vo.KeyVO;
import com.fzq.randapi.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author zfeng
* @description 针对表【User(User table)】的数据库操作Service
* @createDate 2024-09-26 15:36:59
*/
public interface UserService extends IService<User> {

    Long userRegister(UserRegisterDTO userRegisterDTO);

    UserVO userLogin(UserLoginDTO userLoginDTO, HttpServletRequest request);

    Boolean userLogout(HttpServletRequest request);

    UserVO getCurrentUser(HttpServletRequest request);

    KeyVO getKey(KeyGenerateDTO keyGenerateDTO, HttpServletRequest request);
}
