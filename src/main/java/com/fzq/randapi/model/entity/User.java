package com.fzq.randapi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName User
 */
@TableName(value ="User")
@Data
public class User implements Serializable {
    private Long id;

    private String userAccount;

    private String userPassword;

    private Long balance;

    private String accessKey;

    private String secretKey;

    private static final long serialVersionUID = 1L;
}