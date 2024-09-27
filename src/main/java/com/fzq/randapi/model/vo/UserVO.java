package com.fzq.tapibackend.model.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = 4052654175359402528L;

    /**
     * id
     */

    private Long id;

    /**
     * User Name
     */
    private String userName;

    /**
     * User Account
     */
    private String userAccount;

    /**
     * User Avatar
     */
    private String userAvatar;

    /**
     * email
     */
    private String email;

    /**
     * user Role：user / admin
     */
    private String userRole;

    /**
     * user balance, 30 by default on signing up
     */
    private Long balance;

    /**
     * account status（0- normal 1- blocked）
     */
    private Integer status;




}
