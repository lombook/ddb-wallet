package com.jinglitong.springshop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName UserAgentUtils
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/14 11:27
 * @Version 1.0
 **/
public class UserAgentUtils {

    //根据userAgentString 获取机型
    public static String getPhoneType(String userAgentString){
        Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
        Matcher matcher = pattern.matcher(userAgentString);
        String model = null;
        if (matcher.find()) {
            model = matcher.group(1).trim();
        }
        return model;
    }

}
