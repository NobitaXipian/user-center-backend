package com.xipian.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author xipian
 * @date 2023/9/13
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 124260417323224607L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
