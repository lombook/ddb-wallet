package com.jinglitong.springshop.md5;

/**
 * @ClassName Hash
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 16:30
 * @Version 1.0
 **/
public interface Hash extends ByteSource {
    String getAlgorithmName();

    ByteSource getSalt();

    int getIterations();
}

