package com.jinglitong.wallet.server.controller.console;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseBatchController extends BaseController{

    /**
     * 校验手机号
     * @param phone
     * @return
     */
    public String checkPhone(String phone){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phone.length() != 11){
            return  "手机号应为11位数";
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(isMatch){
                return  "ok";
            } else {
                return "手机号是错误格式 ";
            }
        }
    }

    /**
     * 是否位数字
     * @param str
     * @return
     */
    public  boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 是否url
     * @param str
     * @return
     */
    public static boolean isHttpUrl(String str){

        Pattern pattern = Pattern.compile("^((http://)|(https://))([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]{2,6}(:[0-9]{1,5})?(/)");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static void main(String arags[]){
        boolean a = BaseBatchController.isHttpUrl("http://47.104.19.20:3000/");
        System.out.println(a);
    }
}
