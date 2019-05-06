package com.jinglitong.springshop.servcie;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.mapper.PaymenttransactionMapper;
import com.jinglitong.springshop.plugin.PaymentPlugin;
import com.jinglitong.springshop.utils.UuidUtil;

@Service
@Transactional
public class PaymentTransactionService {
	
	 @Autowired
	 private PaymenttransactionMapper paymentTransactionMapper;
	 
	 @Autowired
	 public OrdersMapper ordersMapper;
	 
	 /**
	  * 根基订单zid和支付插件生成支付事务
	  * @param orderId 订单zid
	  * @param paymentPlugin 支付插件
	  * @return
	  */
	 public Paymenttransaction generate(String orderId,PaymentPlugin paymentPlugin){
		 Paymenttransaction paymentTransaction = new Paymenttransaction();
		 paymentTransaction.setOrderId(orderId);
		 paymentTransaction = paymentTransactionMapper.selectOne(paymentTransaction);
			if(paymentTransaction == null){
				paymentTransaction = new Paymenttransaction();
				
				Orders order = new Orders();
				order.setZid(orderId);
				order = ordersMapper.selectOne(order);
				if(order == null){
					return null;
				}
				paymentTransaction.setAmount(order.getAmount());
				paymentTransaction.setOrderId(order.getZid());
				paymentTransaction.setCustId(order.getCustId());
				paymentTransaction.setCurrencyId(order.getCurrencyId());
				paymentTransaction.setExpire(order.getExpire());
				paymentTransaction.setCreatedTime(new Date());
				paymentTransaction.setPaymentpluginid(paymentPlugin.getId());
				paymentTransaction.setPaymentpluginname(paymentPlugin.getName());
				paymentTransaction.setPaystatus(0);//0未支付
				paymentTransaction.setZid(UuidUtil.getUUID());
				paymentTransaction.setStoreId(order.getStoreId());
				paymentTransactionMapper.insert(paymentTransaction);
			}	
			
			paymentTransaction.setPaymentpluginname(paymentPlugin.getName());
			paymentTransactionMapper.updateByPrimaryKeySelective(paymentTransaction);
			return paymentTransaction;		 
	 }

}
