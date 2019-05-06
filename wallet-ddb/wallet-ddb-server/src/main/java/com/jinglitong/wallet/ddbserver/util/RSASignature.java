package com.jinglitong.wallet.ddbserver.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSASignature {
    public static final String KEY_ALGORTHM="RSA";//
    public static final String SIGNATURE_ALGORITHM="MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
    public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥


    public static final String KEY_SHA="SHA";
    public static final String KEY_MD5="MD5";

    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception{
        return (new Base64()).decode(key);
    }

    /**
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key)throws Exception{
        return (new Base64()).encodeAsString(key);
    }

    /**
     * MD5加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data)throws Exception{
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data)throws Exception{
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }


    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initKey()throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 取得公钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)throws Exception{
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得私钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception{
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 用私钥加密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data,String key)throws Exception{
        //解密密钥
        byte[] keyBytes = decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密 * @param data 	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


    /**
     * 用公钥加密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,String key)throws Exception{
        //对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     * 用公钥解密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     *	用私钥对信息生成数字签名
     * @param data	//加密数据
     * @param privateKey	//私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data,String privateKey)throws Exception{
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data	加密数据
     * @param publicKey	公钥
     * @param sign	数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,String publicKey,String sign)throws Exception{
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));

    }

    public static void main(String args[]){
        try {
            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI6vmJJSOSDue2ycvm+EaqxEfhPt5xJFa0g00zyj5YUEt0AvEymWpuXH7v8fPkSNMXyXVyGRvt29/4u0LtuJ+a0ya5fr11bR+q5Dhd7X4xgFWMTSdafYsZ/RMfKHeAlY4tF52Hn7MU+21/EXfA+rU+b72GiZlhWupz3ABgED+voxAgMBAAECgYAme93euSaHZ0sKxEvosAJFbEPFBElqeCazVuwSUvI4qkjymr/EW8mv9/jR/o9A1gQe4oKxnymnjPM7ruFkfRZF4qpnnWZsLj7ZWsV5sVaxuOT9hCcp3qjnIhAMqT4Qr5c4XjUZz0uEgmmG3RAZsedlQeoMZJqlUcA4vlsucRFhQQJBAO/hntUtrwwnBht+pemRlQOc1wFyDxHAPk4Z3tMnb3htKjOn2OcvY+HPZKXQqSiZgRRB1uqKaLLdxPucnabNgC0CQQCYRgtIKi7NQY8bPgTeF4knSt6QWju2ZGg/RJZDe4Ahk30jHrpjeHPFEpw4y6+1DhBDPwA8pP4yrxOjn5nCGOCVAkEA0085vmp+z8K0k5XOC2FuFf+cOq7B7Y4elGgfrHwJqtjnGxfZb1WEtA8ghxy3VodaeIDDFFqWfjHzav38tkpChQJAQ8YBJXho5kBEonpjDjppeAyRrKwMoaxk3av4OUbRi6AqdSQrDzTASjlzQLAfnzUINfXndBZ3cTWNRyk09Te/VQJAZlnCxyqw07FgcLed0Ue0qAENDZ7GHjv2EMsYWeGXpLXMPdE+f8JVwJXfPryQjIFe8qq1LNDQnL/E1eXmL65LUw==";
            String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOr5iSUjkg7ntsnL5vhGqsRH4T7ecSRWtINNM8o+WFBLdALxMplqblx+7/Hz5EjTF8l1chkb7dvf+LtC7bifmtMmuX69dW0fquQ4Xe1+MYBVjE0nWn2LGf0THyh3gJWOLRedh5+zFPttfxF3wPq1Pm+9homZYVrqc9wAYBA/r6MQIDAQAB";

            privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANPWAzz7s/AJDife+FfDmlJ13HE1YTf5NW9wY7tF+apOBjJJMEvVyvkkxc9p8N0jpQg/LZpOP1uuHDnWZgg1BNLcnSKP0c9WpYc82vJwoOZAClZjuVKt5TQYFrWH7k/9JD/4o1rHNCWv45ddbfR2Qv0W8f66IgSx+voXdOQbYuuLAgMBAAECgYEAhKT0wCHoCTzSVVsD7Ii5wDq2/1hqeM8ei5o3iNOHQwG6VZCIomPLxfgluPvga8hfFWviFUY73B+I4xqCJ09zpaU7IwgEdFdTKTsRpc7S5oI8aNSr6hxOQTwSwjYk61rtbdoToYi0xlzKEiNNwEEays4A03yzqyvxyctl3eqKxMkCQQD6p7hpQb4LeejE2vaWfZ4cW4oYzrs7uary1YkTHG2+4hQ/x/st+wXYcNRldDu+LzQ0hipPdZEXA5yZd24M2L9nAkEA2FpivRVAn/IEgCEQ7aP2esNr3GT85DJ5vb4zCDXxSnY+NyXSyti/tmhefIiBguCDTdWHKjTTxucVvp7mQxYwPQJAejdbWJ9AnfHkSFAHmhtIA8WykSQD8Tw3mU37F0FuVXuAqhTFaYcTM5TYS1ugYbw8dTvR14qYoyMUsVOlakgpEwJBAK2ze7zFeFoyC+z3H0k8L+MGTO3o4qUv5VAyiXRZIr2ey3qpUmDrvpfrmZiwXBLAwIrYGaoIOdNiqrGdj89i3B0CQDO3k4iTlqxbsGJ5bmIwfD68xQaVTjwja/bhzGX3qk3iIqH/YR/aGIUGSLANQXo8Ic9SafZihUhnT3ZSGx/IAsA=";
            pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDT1gM8+7PwCQ4n3vhXw5pSddxxNWE3+TVvcGO7RfmqTgYySTBL1cr5JMXPafDdI6UIPy2aTj9brhw51mYINQTS3J0ij9HPVqWHPNrycKDmQApWY7lSreU0GBa1h+5P/SQ/+KNaxzQlr+OXXW30dkL9FvH+uiIEsfr6F3TkG2LriwIDAQAB";

            privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL9Ae0RloGo/aMiFTRzgXys9kTBaamNCLCF421m3lNCQrliFFBD4eBk0us9+sOKpklFmibZrq541nmaYPxPF0nNc3fgORJ++qERRl7YoQrlhKsGNR6ZlhYpuYjm9CViJfNJgptflk40nY3f61JnsSzaYOry0n8MsReYcrxh7XvKTAgMBAAECgYEAvqRYO+XR8hcjLD80jwRZ2ZCVmc2HH0pnEzaV8Oi+vuh54mNFjWUg0n9teEihqk++OsYroFvTxvFL5Jj+x4b6L6vA6TxYrLC8YK2wJgmcJ9srgPD54z0Qkwt7oUoUuK4kInNphd0+M27VEcVx5k9R9x6QC8jmuJj4U0/D3iGtrIECQQDg7XvbK//zsXd5/RRGNZwyxTGhDv0hMVyVYaH/LDumq7LwJaxq/e+IHR+PRd/blJNlB3+rP06BA8aEv6Rt1nAlAkEA2awPnZ1ChcWDYdohwC9nk4Rc1jv3vSKFZBLp2t6rY70OizrpHeY+dOtt8oi5bvr0RIopUGepBhsDiQwtfqieVwJBAMdeeQw6IfL1eLtuMcxRtJPj6iuiR4hmYnQeSCMQKAeB5WerXPqw/uIdPl4WR2gwd71zOhymToVYvVvpKmwZSOkCQHUI6zh+hnbwdKyXIRB5LIKHZ3cUtSYvOnNNLAmvMY2l5/z6Rr75vdkVgH8n/jPqoI19j6sYoSuUOLlmH8v7E28CQQDNOv6o1TagLTGg8DVSaF4szIFx0eUogbVgGuR4lJuGuP8fepK6uWQUOAJMvzaaddDz4zMBhf5r3QNUKTNRcSOw";
            pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/QHtEZaBqP2jIhU0c4F8rPZEwWmpjQiwheNtZt5TQkK5YhRQQ+HgZNLrPfrDiqZJRZom2a6ueNZ5mmD8TxdJzXN34DkSfvqhEUZe2KEK5YSrBjUemZYWKbmI5vQlYiXzSYKbX5ZONJ2N3+tSZ7Es2mDq8tJ/DLEXmHK8Ye17ykwIDAQAB";

//            Map<String,Object> map = initKey();
//            privateKey = getPrivateKey(map);
//            pubKey = getPublicKey(map);
            Long stamp = System.currentTimeMillis();
            String signData = "appkey=167296e0793;timestamp="+stamp;


            String sigV = sign(signData.getBytes(),privateKey);



            boolean flag = verify(signData.getBytes(),pubKey,sigV);
            System.out.println(stamp);
            System.out.println(" "+sigV);
            System.out.println(" "+flag);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
