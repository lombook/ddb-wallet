package com.jinglitong.springshop.md5;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A utility class for computing MD5 hashes.
 *
 * @author zili.zong
 */
public final class Md5Util {
    public static String md5(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static String getNonceStr(String str) {
        MD5 md5Object = new MD5();
        return md5Object.getkeyBeanofStr(str, "UTF-8");
    }

    public static void main(String [] args){
        System.out.println(md5("123456"));
    }
}
