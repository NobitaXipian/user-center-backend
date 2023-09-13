package com.xipian.usercenter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xipian
 * @date 2023/9/12
 */

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser(){

        User user = new User();
        user.setUsername("xipian");
        user.setUserAccount("233");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("222");
        user.setEmail("333");
        boolean result = userService.save(user);

        System.out.println(user.getId());

        assertTrue(result);

    }

    @Test
    void userRegister() {

        long result = userService.userRegister("", "12345678", "12345678");
        assertEquals(-1,result);
        result = userService.userRegister("aaa", "12345678", "12345678");
        assertEquals(-1,result);
        result = userService.userRegister("aaaa", "123", "123");
        assertEquals(-1,result);
        result = userService.userRegister("aaaa((", "12345678", "12345678");
        assertEquals(-1,result);
        result = userService.userRegister("aaaa", "12345678", "12345689");
        assertEquals(-1,result);
        result = userService.userRegister("西片123", "12345678", "12345678");
        assertEquals(-1,result);
        result = userService.userRegister("高木同学", "12345678", "12345678");
        assertTrue(result>0);

    }
}