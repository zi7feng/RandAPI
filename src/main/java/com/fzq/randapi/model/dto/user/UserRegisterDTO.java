package com.fzq.randapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID = 1620524957106567594L;

    /**
     * User Account
     */
    private String userAccount;


    private String userPassword;

    private String checkPassword;


}
