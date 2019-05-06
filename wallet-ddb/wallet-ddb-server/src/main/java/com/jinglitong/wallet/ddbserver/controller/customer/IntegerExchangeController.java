package com.jinglitong.wallet.ddbserver.controller.customer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbIntegExchangeRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegExchangeRule;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.view.ChainExchangeVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegExchangeRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRecordVo;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegExchangeRuleMapper;
import com.jinglitong.wallet.ddbserver.service.IntegerExchangeService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.NumberValidationUtils;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: IntegerExchangeController.java
 * 作        者: yxl 2018年10月18日
 * 创建时间: 2018年10月18日
 * 功能说明:树贝、苗贝兑换股权、金贝兑换现贝
 */
@Slf4j
@RestController
@RequestMapping("/ddb/exchange")
public class IntegerExchangeController {
	@Autowired
	private IntegerExchangeService integerExchangeService; 
	
	@Autowired
	private DdbIntegExchangeRuleMapper ddbStockExchangeRuleMapper;
	
	
	/**
	 * 
	 * 功能说明:app获取股权list
	 * @param ruleType 规则类型(1、树贝兑换 2、苗贝兑换)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getStockRuleList.json",method = RequestMethod.POST)
	public Map getStockRuleList(@RequestBody ChainExchangeVo vo) {
		return  JsonUtil.toJsonSuccess("查询成功", integerExchangeService.getExRule(vo.getRuleName()));
	}
	/**
	 * 
	 * 功能说明:
	 * @param stock
	 * seRuleId ruleName
	 * amount 个数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/stockExchange.json",method = RequestMethod.POST)
	public Map  inteExchange(@RequestBody ChainExchangeVo vo) {
		List<DdbIntegExchangeRule> list = integerExchangeService.getExRule(vo.getRuleName());
		DdbIntegExchangeRule rule = list.get(0);
		if(Integer.parseInt(vo.getAmount()) < rule.getMinAmount() || Integer.parseInt(vo.getAmount()) % rule.getMinAmount() != 0) {
			return JsonUtil.toJsonError( ErrorEnum.EROR_42102);
		}
		DdbIntegralWallet wallet =integerExchangeService.isEnough(vo,rule);
		if(wallet == null) {
			return JsonUtil.toJsonError( ErrorEnum.ERROR_41004);
		}
		log.info(wallet.getCustId()+"用户开始兑换，使用积分："+rule.getIntegName()+",兑换积分："+rule.getExIntegName());
		integerExchangeService.exChange(vo,rule,wallet);
		log.info(wallet.getCustId()+"用户结束兑换");
		return JsonUtil.toJsonSuccess("兑换成功成功", null);
	}
	
	/*****************************************************金贝兑换现贝**************************************************************/
	/**
	 * 
	 * 功能说明:金贝兑换现贝
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/jinToXianExchange.json",method = RequestMethod.POST)
	public Map jinToXianExchange(@RequestBody ChainExchangeVo vo) {
		List<DdbIntegExchangeRule> list = integerExchangeService.getExRule(vo.getRuleName());
		DdbIntegExchangeRule rule = list.get(0);
		if(list.size() != 1) {
			return JsonUtil.toJsonError( ErrorEnum.EROR_42300);
		}
		if(!NumberValidationUtils.isPositiveInteger(vo.getAmount())) {
			return JsonUtil.toJsonError( ErrorEnum.EROR_42302);
		}
		if(Integer.parseInt(vo.getAmount()) < rule.getMinAmount()  ) {
			return JsonUtil.toJsonError( ErrorEnum.EROR_42301);
		}
		
		Customer cust = (Customer) SessionUtil.getUserInfo();
		//cust.setCustId("ffe14deec12a486c9a53b6ca90527f69");
		
		boolean flag = integerExchangeService.InteIsEnough(cust.getCustId(), vo.getAmount(), 
				Integer.parseInt(vo.getAmount()) * rule.getChargePercent().intValue() , rule);
		if(!flag) {
			return JsonUtil.toJsonError( ErrorEnum.EROR_42303);
		}
		integerExchangeService.jinExchange(vo, rule);
		return JsonUtil.toJsonSuccess("兑换成功成功", null);
	}
	
	/**
	 * 
	 * 功能说明:获取金贝兑换现贝记录
	 * @param vo
	 * @return
	 */
	@RequestMapping(value ="/getExRecord.json",method = RequestMethod.POST)
	public Map getWithDrawRecord(@RequestBody DdbIntegExchangeRecordVo vo) {
		Map map = integerExchangeService.getRecord(vo);
		return JsonUtil.toJsonSuccess("获取记录成功", map);
	}
	
}
