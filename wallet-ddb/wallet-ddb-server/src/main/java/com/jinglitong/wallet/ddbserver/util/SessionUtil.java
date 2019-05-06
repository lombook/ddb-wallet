package com.jinglitong.wallet.ddbserver.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class SessionUtil {
    /**
     * 获取授权主要对象
     */
    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        try{
            Session session = getSubject().getSession();
            if (session == null){
                session = getSubject().getSession();
            }
            if (session != null){
                return session;
            }
        }catch (InvalidSessionException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Object getUserInfo(){
        try {
            if(getSession() != null){
                return getSubject().getPrincipals().getPrimaryPrincipal();
            }else{
                return null;
            }
        }catch (Exception e){

        }
        return null;
    }

    // ============== Shiro Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj==null?defaultValue:obj;
    }

    public static void putCache(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        getSession().removeAttribute(key);
    }
}
