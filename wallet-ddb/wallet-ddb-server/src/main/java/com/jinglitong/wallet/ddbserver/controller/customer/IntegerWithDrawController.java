package com.jinglitong.wallet.ddbserver.controller.customer;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.xml.ws.RequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.CustomerBank;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRuleVo;
import com.jinglitong.wallet.ddbapi.model.view.WithDrawVo;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.service.IntegerWithDrawService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: IntegerWithDrawController.java
 * 作        者: yxl 2018年11月21日
 * 创建时间: 2018年11月21日
 * 功能说明: 现贝提现
 */
@RestController
@RequestMapping("/withDraw")
@Slf4j
public class IntegerWithDrawController {

	@Autowired
	private IntegerWithDrawService integerWithDrawService;
	
	/**
	 * 
	 * 功能说明:根据规则查询提现规则
	 * @param record
	 * @return
	 */
	@RequestMapping(value ="/getWithDrowRule.json",method = RequestMethod.POST)
	public Map getRule(@RequestBody DdbIntegralWithdrawRule param) {
		DdbIntegralWithdrawRuleVo rule = integerWithDrawService.getRuleByName(param.getRuleName());
		log.info("获取提现规则成功");
		return JsonUtil.toJsonSuccess("获取提现规则成功", rule);
	}
	
	/**
	 * 
	 * 功能说明:获取用户提现记录
	 * @return
	 */
	@RequestMapping(value ="/getWithDrawRecord.json",method = RequestMethod.POST)
	public Map getWithDrawRecord(@RequestBody DdbIntegralWithdrawRecordVo vo) {
		Map map = integerWithDrawService.getWithdrawRecord(vo);
		return JsonUtil.toJsonSuccess("获取提现记录成功", map);
	}
	
	/**
	 * 
	 * 功能说明:申请提现
	 * @return
	 */
	@RequestMapping(value ="/withDrawApply.json",method = RequestMethod.POST)
	public Map withDrawApply(@RequestBody WithDrawVo vo) {
		Customer cust = (Customer) SessionUtil.getUserInfo();
		//cust.setCustId("fff71db2d7ce4b0caf7a3fac3a4c2574");
		DdbIntegralWithdrawRuleVo rule = integerWithDrawService.getRuleByName(vo.getRuleName());
		//检查规则
		if(rule != null) {
			CustomerBank userBank =integerWithDrawService.getCustBank(cust.getCustId());
			//检查银行卡
			if(userBank != null ) {
				//检查是否低于最小提现金额
				int size = new BigDecimal(vo.getAmount()).compareTo(new BigDecimal(rule.getMinAmount()));
				if(size != -1) {
					//检查用户的现贝和宝分是否足够扣除
					Boolean flag =integerWithDrawService.getCustWallet(cust.getCustId(),vo.getAmount(),rule.getIntegCharge(),rule);
					if(flag) {
						integerWithDrawService.withDrawApply(rule, vo.getAmount(), cust.getCustId(), userBank);
					}else {
						return JsonUtil.toJsonError(ErrorEnum.EROR_42003);
					}
				}else {
					return JsonUtil.toJsonError(ErrorEnum.ERROR_42002);
				}
			}else {
				return JsonUtil.toJsonError(ErrorEnum.ERROR_42001);
			}
		}else {
			return JsonUtil.toJsonError(ErrorEnum.ERROR_42000);
		}
		
		
	    return JsonUtil.toJsonSuccess("提现成功", null);
	}
}
