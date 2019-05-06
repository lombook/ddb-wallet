package com.jinglitong.wallet.ddbserver.controller.customer;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRule;
import com.jinglitong.wallet.ddbapi.model.view.ChainExchangeVo;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.service.ChainExchangeServcice;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ChainExchangeController.java
 * 作        者: yxl 2018年11月25日
 * 创建时间: 2018年11月25日
 * 功能说明:宝分兑换MEI 
 */
@RestController
@Slf4j
@RequestMapping("/coinExchange")
public class ChainExchangeController {

	@Autowired
	private ChainExchangeServcice chainExchangeServcice;
	
	/**
	 * 
	 * 功能说明: 获取兑换规则
	 * @param rule
	 * @return
	 */
	@RequestMapping(value ="/getChainRule.json",method = RequestMethod.POST)
	public Map getChainRule(@RequestBody DdbChainExchangeRule rule ) {
		
		return JsonUtil.toJsonSuccess("获取兑换规则成功", chainExchangeServcice.getChainRule(rule.getRuleName()));
	}
	/**
	 * 
	 * 功能说明: 兑换申请
	 * @param vo
	 * @return
	 */
	@RequestMapping(value ="/exChangeApply.json",method = RequestMethod.POST)
	public Map exChangeApply(@RequestBody ChainExchangeVo vo) {
		Customer cust = (Customer) SessionUtil.getUserInfo();
		//cust.setCustId("0027f2f99a8c4f99b57f5091d2a85cf0");
		DdbChainExchangeRule rule = chainExchangeServcice.getChainRule(vo.getRuleName());
		//检验规则是否存在
		if(rule != null) {
			int size = new BigDecimal(vo.getAmount()).compareTo(new BigDecimal(rule.getMinAmount()));
			//校验最低兑换数额
			if( size != -1 ) {
				//校验是否是100的整数倍 
				 if(Integer.valueOf(vo.getAmount()) % rule.getMinAmount()  == 0) {
					 Boolean flag = chainExchangeServcice.getCustWallet(cust.getCustId(),rule,vo.getAmount());
					 if(flag) {
						 chainExchangeServcice.exChangeApply(cust.getCustId(), rule, vo.getAmount());
					 }else {
						 return JsonUtil.toJsonError(ErrorEnum.EROR_42103);
					 }
				 }else {
					 return JsonUtil.toJsonError(ErrorEnum.EROR_42101);
				 }
			}else {
				return JsonUtil.toJsonError(ErrorEnum.EROR_42102);
			}
		}else {
			return JsonUtil.toJsonError(ErrorEnum.EROR_42100);
		}
		return JsonUtil.toJsonSuccess("兑换申请成功", "");
	}
	 
}