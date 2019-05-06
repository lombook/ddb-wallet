package com.jinglitong.springshop.md5;

/**
 * @ClassName MD5ShiroUtils
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 15:23
 * @Version 1.0
 **/
public class MD5ShiroUtils {
    public static String createBusinessPwd(String password, String salt){
        return new SimpleHash("md5",password, ByteSource.Util.bytes(salt),2).toHex();
    }

    public static String createCustomPwd(String password, String salt){
        return new SimpleHash("md5",password,ByteSource.Util.bytes(salt),1).toHex();
    }
    public static String createRedisKey(String key, String salt){
        return new SimpleHash("md5",key,ByteSource.Util.bytes(salt),3).toHex();
    }
    public static void main(String[] args) {
        System.out.println(createBusinessPwd("123456","255f8176d5fe413cb8da666798b8ffcb"));
        System.out.println(createCustomPwd("123456","255f8176d5fe413cb8da666798b8ffcb"));
        System.out.println(createRedisKey("123456","255f8176d5fe413cb8da666798b8ffcb"));
    }
}
