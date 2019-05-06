package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Sku;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SkuMapper extends MyMapper<Sku> {
    Sku selectByZid(@Param("zid") String skuId);

    Map<String,Object> selectByPriceAndWeight(String skuId);
    
    String selectStoreIdbyZid(@Param("zid") String skuId);
}