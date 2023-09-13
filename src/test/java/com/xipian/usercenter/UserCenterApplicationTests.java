package com.xipian.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {


    @Test
    void contextLoads() {


    }

    @Test
    void testValidPattern(){
        String validPattern = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";

        Matcher matcher = Pattern.compile(validPattern).matcher("   aaa");
        if (!matcher.find()){
            System.out.println("err");
        }
        matcher = Pattern.compile(validPattern).matcher("---a");
        if (!matcher.find()){
            System.out.println("err");
        }
        matcher = Pattern.compile(validPattern).matcher("===aaa");
        if (!matcher.find()){
            System.out.println("err");
        }
        matcher = Pattern.compile(validPattern).matcher("```~~~~aaa");
        if (!matcher.find()){
            System.out.println("err");
        }
        matcher = Pattern.compile(validPattern).matcher("aa");
        if (!matcher.find()){
            System.out.println("err");
        }else {
            System.out.println("yes");
        }
        matcher = Pattern.compile(validPattern).matcher("西片aa_1213");
        if (!matcher.find()){
            System.out.println("err");
        }else {
            System.out.println("yes");
        }
    }


    @Test
    void testEncrypt(){
        final String SALT = "xipian";
        String encryptPassword = DigestUtils.md5DigestAsHex(("SALT"+"userPassword").getBytes());
        System.out.println(encryptPassword);
    }
}
