package com.xipian.usercenter.service.impl;
import java.util.Date;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xipian.usercenter.model.domain.User;
import com.xipian.usercenter.service.UserService;
import com.xipian.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author NOBITA
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-09-12 16:43:18
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     *  盐值
     */

    private static final String SALT = "xipian";

    /**
     * 用户登录态键
     */
    private static final String USER_LOGIN_STATE = "userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        if (StringUtils.isAllBlank(userAccount,userPassword,checkPassword)){
            //TODO 抛出自定义异常类
            return -1;
        }

        if (userAccount.length()<4){
            return -1;
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }

        //校验账号不包含特殊字符
        //只包含中文英文数字下划线有效
        String validPattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            return -1;
        }

        if (!userPassword.equals(checkPassword)){
            return -1;
        }

        //校验账户名不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        int count = this.count(userQueryWrapper);
        if (count > 0){
            return -1;
        }

        //加密密码

        String encryptPassword = DigestUtils.md5DigestAsHex(("SALT"+userPassword).getBytes());

        //数据库插入用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean saveResult = this.save(user);

        if (!saveResult){
            return -1;
        }

        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {

        if (StringUtils.isAllBlank(userAccount,userPassword)){
            return null;
        }

        if (userAccount.length()<4){
            return null;
        }

        if (userPassword.length() < 8 ){
            return null;
        }

        //校验账号不包含特殊字符
        //只包含中文英文数字下划线有效
        String validPattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            return null;
        }

        //加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex(("SALT"+userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        userQueryWrapper.eq("userPassword",userPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        //用户不存在或账号密码不匹配
        if (user == null){
            log.info("user login failed, userAccount cannot match the userPassword");
            return null;
        }
        //用户信息脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }
}



