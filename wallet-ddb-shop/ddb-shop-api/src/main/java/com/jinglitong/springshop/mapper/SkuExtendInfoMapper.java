package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.SkuExtendInfo;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface SkuExtendInfoMapper extends MyMapper<SkuExtendInfo> {

    int updateBySkuId(@Param("skuPriceId") String skuPriceId,@Param("svalue") BigDecimal sValue,@Param("djbvalue") BigDecimal djbValue);
}