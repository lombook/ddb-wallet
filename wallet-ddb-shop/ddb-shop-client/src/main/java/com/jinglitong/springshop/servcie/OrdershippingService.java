package com.jinglitong.springshop.servcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.mapper.CartMapper;
import com.jinglitong.springshop.mapper.OrdershippingMapper;
import com.jinglitong.springshop.vo.response.OrderShopingVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class OrdershippingService {
	@Autowired
    private OrdershippingMapper ordershippingMapper;
	
	public OrderShopingVo getOrdershippingDetail(String zid){  
		return ordershippingMapper.selectOrderShippingByZid(zid);
    }

}
