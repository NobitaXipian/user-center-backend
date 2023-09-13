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
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -368772850870473779L;

    private String userAccount;

    private String userPassword;

}
