package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.OrderRealPay;
import com.jinglitong.springshop.mapper.OrderRealPayMapper;
import com.jinglitong.springshop.utils.UuidUtil;

@Service
@Transactional
public class OrderRealPayService {
	@Autowired
    private OrderRealPayMapper orderRealPayMapper;
	public int add(String orderId,String currency_id,BigDecimal amount,String currency_code,int currency_type){
		Date date = new Date();
		OrderRealPay orderRealPay = new OrderRealPay();
       	orderRealPay.setZid(UuidUtil.getUUID());
        orderRealPay.setCurrencyCode(currency_code);
        orderRealPay.setCurrencyId(currency_id);
        orderRealPay.setAmount(amount);
        orderRealPay.setCurrencyType(currency_type);
        orderRealPay.setOrderId(orderId);
        orderRealPay.setCreateTime(date);
        orderRealPay.setUpdateTime(date);
        return orderRealPayMapper.insert(orderRealPay);
	}

}
