package com.jinglitong.springshop.service;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.entity.CustomerTeamRecord;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.mapper.CustomerMapper;
import com.jinglitong.springshop.mapper.CustomerTeamRecordMapper;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.utils.UuidUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: CustomerTeamService.java
 * 作        者: yxl 2019年4月2日
 * 创建时间: 2019年4月2日
 * 功能说明:订单业绩统计
 */
@Service
@Slf4j
public class CustomerTeamService {

	@Resource
	private OrdersMapper ordersMapper;
	
	@Resource
	private CustomerMapper customerMapper;
	
	@Resource
	private CustomerTeamRecordMapper customerTeamRecordMapper; 
	
	public void  cusTeam(String orderId) {
		CustomerTeamRecord teamRecord = new CustomerTeamRecord();
		teamRecord.setOrderId(orderId);
		List<CustomerTeamRecord> recList = customerTeamRecordMapper.select(teamRecord);
		if(recList.size() > 0) {
			log.info("订单已处理:{}",orderId);
			return ;
		}
		Orders order = new Orders();
		order.setZid(orderId);
		order.setOrderParent("P");
		order = ordersMapper.selectOne(order);
		if(order != null) {
			log.info("订单:{}团队绩效开始",orderId);
			List<CustomerTeamRecord> teamList = new LinkedList<>();
			List<String> custList =  getCustRelation(order.getCustId());
			for (String str : custList) {
				log.info(str);
				String [] arr = str.split("-");
				CustomerTeamRecord team = new CustomerTeamRecord();
				team.setZid(UuidUtil.getUUID());
				team.setCustId(arr[0]);
				team.setAccount(arr[1]);
				if("CNY".equals(order.getPriceCode())) {
					team.setPriceCny(order.getPrice());
				}else if("AUD".equals(order.getPriceCode())) {
					team.setPriceAud(order.getPrice());
				}
				if(order.getIntegralPrice() != null) {
					team.setPriceDjb(order.getIntegralPrice());
				}
				team.setOrderSn(order.getSn());
				team.setOrderId(orderId);
				team.setOrderDate(order.getCreateTime());
				team.setCreateTime(new Date());
				teamList.add(team);
			}
			int count = customerTeamRecordMapper.insertList(teamList);
			log.info("订单:{}团队绩效结束,入库{count}条",orderId,count);
		}else {
			log.info("订单表无此订单:{} ",orderId);
		}
		
	}
	
	public List<String> getCustRelation(String custId){
		List<String> custList = new LinkedList<>();
		Customer cust = new Customer();
		cust.setCustId(custId);
		cust = customerMapper.selectOne(cust);
		custList.add(custId+"-"+cust.getAccount());
		if(StringUtils.isEmpty(cust.getInviteCode())) {
			return custList;
		}
		recursion(cust.getInviteCode(), custList);
		return custList;
	}
	
	private void recursion(String inviteCode,List<String> custList) {
		Customer cust = new Customer();
		cust.setSelfInvite(inviteCode);
		cust = customerMapper.selectOne(cust);
		if(cust != null) {
			custList.add(cust.getCustId()+"-"+cust.getAccount());
			if(StringUtils.isNotEmpty(cust.getInviteCode())) {
				recursion(cust.getInviteCode(), custList);
			}
		}
	}
}
