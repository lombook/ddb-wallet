package com.jinglitong.springshop.md5;

/**
 * @ClassName Md2Hash
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 16:47
 * @Version 1.0
 **/
public class Md2Hash extends SimpleHash {
    public static final String ALGORITHM_NAME = "MD2";

    public Md2Hash() {
        super("MD2");
    }

    public Md2Hash(Object source) {
        super("MD2", source);
    }

    public Md2Hash(Object source, Object salt) {
        super("MD2", source, salt);
    }

    public Md2Hash(Object source, Object salt, int hashIterations) {
        super("MD2", source, salt, hashIterations);
    }

    public static Md2Hash fromHexString(String hex) {
        Md2Hash hash = new Md2Hash();
        hash.setBytes(Hex.decode(hex));
        return hash;
    }

    public static Md2Hash fromBase64String(String base64) {
        Md2Hash hash = new Md2Hash();
        try {
            hash.setBytes(Base64.decode(base64));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
}

