package com.xipian.usercenter.service;

import com.xipian.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author NOBITA
* @description 针对表【user】的数据库操作Service
* @createDate 2023-09-12 16:43:18
*/
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return  新用户ID
     */
    long  userRegister(String userAccount,String userPassword,String checkPassword);

    User doLogin(String userAccount, String userPassword, HttpServletRequest request);


}
