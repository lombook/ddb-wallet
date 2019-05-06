package com.jinglitong.springshop.md5;


import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;

/**
 * @ClassName PasswordUtil
 * @Description TODO MD5加盐加密
 * @Author zili.zong
 * @Date 2019/1/9 13:40
 * @Version 1.0
 **/
public class PasswordUtil {
    /**
     * 生成含有盐的密码
     */
    public static String generate(String password,String salt) {
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }
    /**
     * 校验密码是否正确
     */
    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }
    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 加密+加盐 cY7jx%2FB%2FBeHp8p2c6kpBiA%3D%3D

        String salt = "931903e80d3442112aa2391f1ba0573c";
        System.out.println("salt = "+salt);
        String password1 = generate("123456",salt);
        if("0f413009a8d1cd01a400fed40f442829".equals(password1)){
            System.out.println("same");
        }else {
            System.out.println("not same");
        }
        System.out.println("结果：" + password1 + "   长度："+ password1.length());
        // 解码
        System.out.println(verify("admin", password1));
        // 加密+加盐
        String password2= generate("admin",salt);
        System.out.println("结果：" + password2 + "   长度："+ password2.length());
        // 解码
        System.out.println(verify("admin", password2));
    }
}