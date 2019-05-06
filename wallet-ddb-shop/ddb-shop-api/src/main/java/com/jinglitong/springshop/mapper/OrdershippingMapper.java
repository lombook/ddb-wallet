package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Ordershipping;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.response.OrderShopingVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdershippingMapper extends MyMapper<Ordershipping> {

    List<Ordershipping> selectByOrderZid(@Param("zid") String zid);
    
    OrderShopingVo selectOrderShippingByZid(String zid);
}