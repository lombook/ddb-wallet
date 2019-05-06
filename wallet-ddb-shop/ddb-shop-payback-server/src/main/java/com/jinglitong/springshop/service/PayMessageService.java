package com.jinglitong.springshop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.PayMessage;
import com.jinglitong.springshop.mapper.PayMessageMapper;

@Service
@Transactional
public class PayMessageService {
	@Autowired
	private PayMessageMapper payMessageMapper;
	
	public int add(PayMessage payMessage){
		return payMessageMapper.insert(payMessage);
	}

	public int add(String payactionId ,String type,String body){
		PayMessage payMessage = new PayMessage();
		payMessage.setPayactionId(payactionId);
		payMessage.setType(new Byte(type));
		payMessage.setCreateTime(new Date());
		payMessage.setBody(body);
		return payMessageMapper.insert(payMessage);
	}
	

}
