package com.xipian.usercenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xipian.usercenter.common.ErrorCode;
import com.xipian.usercenter.exception.BusinessException;
import com.xipian.usercenter.mapper.UserMapper;
import com.xipian.usercenter.model.domain.User;
import com.xipian.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xipian.usercenter.constant.userConstant.USER_LOGIN_STATE;

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



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");

        }

        //校验账号不包含特殊字符
        //只包含中文英文数字下划线有效
        String validPattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号不能包含特殊字符");
        }

        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户两次输入密码不一致");
        }

        //校验账户名不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount",userAccount);
        int count = this.count(userQueryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号重复");
        }

        //加密密码

        String encryptPassword = DigestUtils.md5DigestAsHex(("SALT"+userPassword).getBytes());

        //数据库插入用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean saveResult = this.save(user);

        if (!saveResult){
            throw new BusinessException(ErrorCode.UPDATE_ERROR,"未保存用户");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }

        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }

        if (userPassword.length() < 8 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }

        //校验账号不包含特殊字符
        //只包含中文英文数字下划线有效
        String validPattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号不能包含特殊字符");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或账号密码不匹配");
        }
        //用户信息脱敏
        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }

    /**
     * 用户信息脱敏
     *
     * @param originUser
     * @return 脱敏后的用户实例
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




