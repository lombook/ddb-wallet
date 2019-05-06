package com.jinglitong.springshop.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
/**
 * @ClassName MyMapper
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 16:54
 * @Version 1.0
 **/
public interface MyMapper <T> extends Mapper<T>, MySqlMapper<T>{
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
