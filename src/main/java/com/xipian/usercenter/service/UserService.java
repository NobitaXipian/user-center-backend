package com.xipian.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xipian.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author NOBITA
* @description 针对表【user】的数据库操作Service
* @createDate 2023-09-12 16:43:18
*/
public interface UserService extends IService<User> {

    /**
     *用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return  新用户ID
     */
    long  userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户实例
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param originUser
     * @return 脱敏后的用户实例
     */
    User getSafetyUser(User originUser);
}
