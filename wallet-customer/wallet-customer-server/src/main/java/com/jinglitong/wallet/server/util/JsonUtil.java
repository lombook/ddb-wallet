package com.jinglitong.wallet.server.util;

import com.jinglitong.wallet.server.common.ErrorEnum;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public JsonUtil() {
    }

    public static Map toJsonSuccess(String msg, Object obj){
        Map<String,Object> mp = new HashMap<String, Object>();
        mp.put("code", 0);
        mp.put("message", msg);
        mp.put("data", obj);
        return mp;
    }

    public static Map toJsonSuccess(String msg){
        return toJsonSuccess(msg, null);
    }

    public static Map toJsonError(Integer code,String msg,Object obj){
        Map<String,Object> mp = new HashMap<String, Object>();
        mp.put("code", code);
        mp.put("message", msg);
        mp.put("data", obj);
        return mp;
    }
    public static Map toJsonError(Integer code,String msg){
        Map<String,Object> mp = new HashMap<String, Object>();
        mp.put("code", code);
        mp.put("message", msg);
        mp.put("data", null);
        return mp;
    }

    public static Map toJsonError(ErrorEnum err){
        Map<String,Object> mp = new HashMap<String, Object>();
        mp.put("code", err.getCode());
        mp.put("message", err.getMsg());
        mp.put("data", null);
        return mp;
    }

    public static Map toJsonError(ErrorEnum err,Object obj){
        Map<String,Object> mp = new HashMap<String, Object>();
        mp.put("code", err.getCode());
        mp.put("message", err.getMsg());
        mp.put("data", obj);
        return mp;
    }
}
