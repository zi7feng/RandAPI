package com.fzq.randapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.randapi.common.ErrorCode;
import com.fzq.randapi.common.PasswordUtils;
import com.fzq.randapi.exception.BusinessException;
import com.fzq.randapi.model.dto.user.KeyGenerateDTO;
import com.fzq.randapi.model.dto.user.UserLoginDTO;
import com.fzq.randapi.model.dto.user.UserRegisterDTO;
import com.fzq.randapi.model.entity.User;
import com.fzq.randapi.model.vo.KeyVO;
import com.fzq.randapi.model.vo.UserVO;
import com.fzq.randapi.service.UserService;
import com.fzq.randapi.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fzq.randapi.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author zfeng
* @description 针对表【User(User table)】的数据库操作Service实现
* @createDate 2024-09-26 15:36:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private static final String SALT = "fzq";


    @Autowired
    private UserMapper userMapper;

    @Override
    public Long userRegister(UserRegisterDTO userRegisterDTO) {
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the user account cannot be less than 4");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the password cannot be less than 8");
        }

        // no special characters
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The user account cannot contain special characters");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The two passwords are not matched");
        }

        // duplicate account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(queryWrapper);

        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account already exists.");
        }

        // encrypt
        String encryptPassword = PasswordUtils.encryptPassword(userPassword, SALT);        // is account exist
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setBalance(30L);
        boolean saveResult = this.save(user);
        if (saveResult) {
            return user.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "register failed");
        }

    }

    @Override
    public UserVO userLogin(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // encrypt
        String encryptPassword = PasswordUtils.encryptPassword(userPassword, SALT);        // is account exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);


        // doesn't exist
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Wrong account or password.");
        }
        UserVO safeUser = getSafeUser(user);

        // 4. set login state
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, safeUser);
        session.setMaxInactiveInterval(60 * 60); // expire time: 60 * 60s

        return safeUser;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getCurrentUser(HttpServletRequest request) {
        UserVO user = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return user;    }

    @Override
    public KeyVO getKey(KeyGenerateDTO keyGenerateDTO, HttpServletRequest request) {
        if (keyGenerateDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        // get current user data
        UserVO userVO = getCurrentUser(request);
        if (userVO == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // whether already has accesskey
        User existingUser = userMapper.selectById(userVO.getId());
        if (existingUser != null && existingUser.getAccessKey() != null && !existingUser.getAccessKey().isEmpty()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // whether the accesskey exists in database(must unique)
        String accessKey = keyGenerateDTO.getAccessKey();
        User userWithSameAccessKey = userMapper.selectOne(new QueryWrapper<User>().eq("access_key", accessKey));
        if (userWithSameAccessKey != null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Duplicate accesskey");
        }


        // generate secretkey
        String secretKey = PasswordUtils.encryptPassword(accessKey, userVO.getUserAccount());        // is account exist
        // update user's data
        User user = new User();
        user.setId(userVO.getId());
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);

        int rows = userMapper.update(user, new UpdateWrapper<User>().eq("id", userVO.getId())
                .set("access_key", accessKey)
                .set("secret_key", secretKey));

        if (rows == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // return secretkey
        KeyVO keyVO = new KeyVO();
        keyVO.setSecretKey(secretKey);
        return keyVO;
    }

    private UserVO getSafeUser(User user) {
        UserVO safeUser = new UserVO();
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setId(user.getId());

        return safeUser;
    }
}




