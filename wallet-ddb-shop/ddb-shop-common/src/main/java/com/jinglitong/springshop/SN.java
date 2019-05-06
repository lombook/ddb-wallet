package com.jinglitong.springshop;

/**
 * @author fyy
 * @create 2019-01-21-15:08}
 */
public class SN {
    public static String OrderSn(int num){
        long time = System.currentTimeMillis() / 1000;
        return time+randomNum(num);
    }

    public static String randomNum(int num){
        StringBuilder stringBuilder = new StringBuilder();
        String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] m = model.toCharArray();
        for (int j = 0; j < num; j++) {
            char c = m[(int) (Math.random() * 36)];
            // 保证六位随机数之间没有重复的
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
