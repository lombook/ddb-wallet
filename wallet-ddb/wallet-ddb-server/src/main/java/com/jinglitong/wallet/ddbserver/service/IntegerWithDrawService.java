package com.jinglitong.wallet.ddbserver.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.CustomerBank;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRuleVo;
import com.jinglitong.wallet.ddbserver.mapper.CustomerBankMapper;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWithdrawRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWithdrawRuleMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: IntegerWithDrawService.java
 * 作        者: yxl 2018年11月22日
 * 创建时间: 2018年11月22日
 * 功能说明:现贝提现service
 */
@Service
public class IntegerWithDrawService {

	@Autowired
	private DdbIntegralWithdrawRuleMapper ddbIntegralWithdrawRuleMapper; 
	
	@Autowired
	private DdbIntegralWithdrawRecordMapper DdbIntegralWithdrawRecordMapper;
	
	@Autowired
	private CustomerBankMapper customerBankMapper;
	
	@Autowired
	private DdbIntegralWalletMapper DdbIntegralWalletMapper;
	
	@Autowired
	private IntegerExchangeService integerExchangeService;
	
	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	
	@Autowired
	private CustomerMapper customerMapper; 
	
	/**
	 * 
	 * 功能说明:获取提现规则
	 * @param ruleName
	 * @return
	 */
	public DdbIntegralWithdrawRuleVo getRuleByName(String ruleName) {
    	DdbIntegralWithdrawRuleVo rule =  ddbIntegralWithdrawRuleMapper.getRuleByName(ruleName);
    	if(rule != null) {
    		rule.setIntegCharge(rule.getIntegCharge() / 100);
    		rule.setMinAmount(rule.getMinAmount() / 100);
    	}
		return rule;
	}
	
	/**
	 * 
	 * 功能说明:获取用户的提现记录
	 * @param vo
	 * @return
	 */
	public Map getWithdrawRecord(DdbIntegralWithdrawRecordVo vo){
		Customer cust = (Customer) SessionUtil.getUserInfo();
		if (vo.getPage() != null && vo.getRows() != null) {
			PageHelper.startPage(vo.getPage(), vo.getRows());
		}
		vo.setCustId(cust.getCustId());
		List<DdbIntegralWithdrawRecordVo> list = DdbIntegralWithdrawRecordMapper.getCustWithDrawRecord(vo);
		PageInfo pageinfo = new PageInfo<>(list);
		HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("list",list);
		return map;
	}
	/**
	 * 
	 * 功能说明:获取用户的银行卡
	 * @param custId
	 * @return
	 */
	public CustomerBank getCustBank(String custId) {
		Weekend<CustomerBank>weekend = Weekend.of(CustomerBank.class);
    	WeekendCriteria<CustomerBank,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(CustomerBank::getCustId, custId);
    	Criteria.andEqualTo(CustomerBank::getFlag, true);
    	CustomerBank bank = customerBankMapper.selectOneByExample(weekend);
		return bank;
	}
	/**
	 * 
	 * 功能说明:检查用户钱包余额是否充足
	 * @param custId
	 * @param xb
	 * @param bf
	 * @return
	 */
	public Boolean getCustWallet(String custId,String xb,Integer bf,DdbIntegralWithdrawRuleVo rule){
		Weekend<DdbIntegralWallet>weekend = Weekend.of(DdbIntegralWallet.class);
    	WeekendCriteria<DdbIntegralWallet,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition("cust_id = '"+custId+"' and (inte_name ='"+rule.getIntegName()+"' or inte_name = '"+rule.getChargeIntegName()+"')");
		List<DdbIntegralWallet> list = DdbIntegralWalletMapper.selectByExample(weekend);
		BigDecimal serviceAmount =rule.getServicePercent().multiply(new BigDecimal(xb)).multiply(new BigDecimal(100));
		BigDecimal xbAmount = new BigDecimal(xb).multiply(new BigDecimal(100)).add(serviceAmount);//总共需要的现贝
		BigDecimal bfAmount = new BigDecimal(bf).multiply(new BigDecimal(100));
		
		for (DdbIntegralWallet ddbIntegralWallet : list) {
			BigDecimal amount = new BigDecimal(ddbIntegralWallet.getAmount());
			if(rule.getIntegName().equals(ddbIntegralWallet.getInteName())) {
				if(amount.compareTo(xbAmount) == -1) {
					return false;
				}
			}else {
				if(amount.compareTo(bfAmount) == -1) {
					return false;
				}
			}
		}
		return true ;
	}
	/**
	 * 
	 * 功能说明:申请提现
	 * @param rule
	 * @param amount
	 * @return
	 */
	@Transactional
	public void withDrawApply(DdbIntegralWithdrawRuleVo rule,String amount,String custId,CustomerBank userBank) {
		Customer cus = customerMapper.selectByCustId(custId);
		Integer xb = new BigDecimal(amount).multiply(new BigDecimal(100)).intValue();
		DdbIntegralWithdrawRecord record =buildBean(rule, cus, userBank,xb);
		DdbIntegralAccount inteAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveIntegRealName());
		integerExchangeService.addInteg(-xb, xb, custId, record.getZid(), inteAcc, "现贝提现",false);
		
		Integer bf = record.getIntegCharge();
		DdbIntegralAccount chargeAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveChargeRealName());
		integerExchangeService.addInteg(-bf, bf, custId,record.getZid(), chargeAcc, "现贝提现手续费",false);
		
		int xbService=rule.getServicePercent().multiply(new BigDecimal(amount)).multiply(new BigDecimal(100)).intValue();
		DdbIntegralAccount serviceAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveServiceTempName());
		integerExchangeService.addInteg(-xbService, xbService, custId,record.getZid(), serviceAcc, "现贝提现服务费费",false);
		int count = DdbIntegralWithdrawRecordMapper.insertSelective(record);
		if(count != 1) {
			throw new RuntimeException("内部错误");
		}
	}
	
	public DdbIntegralWithdrawRecord buildBean (DdbIntegralWithdrawRuleVo rule,Customer cus,CustomerBank userBank,Integer amount) {
		String time = DateUtils.getDateTime();
		DdbIntegralWithdrawRecord bean = new DdbIntegralWithdrawRecord();
		bean.setZid(UuidUtil.getUUID());
		bean.setWithdrawRuleId(rule.getZid());
		bean.setCustId(cus.getCustId());
		bean.setCardholder(userBank.getCardholder());
		bean.setCardNo(userBank.getCardNo());
		bean.setAmount(new BigDecimal(amount).multiply(rule.getPercent()).intValue());
		bean.setIntegAmount(amount);
		bean.setIntegName(rule.getIntegName());
		Integer bf = new BigDecimal(rule.getIntegCharge()).multiply(new BigDecimal(100)).intValue();
		bean.setIntegCharge(bf);
		bean.setIntegChargeName(rule.getChargeIntegName());
		bean.setApplyStatus(0);
		bean.setDrawStatus(0);
		bean.setApplyTime(time);
		bean.setCreateTime(time);
		bean.setUpdateTime(time);
		bean.setAccount(cus.getPhone());
		bean.setActiveAddress(userBank.getActiveAddress());
		bean.setBankName(userBank.getBankName());
		bean.setIntegServiceName(rule.getIntegName());
		int xbService=rule.getServicePercent().multiply(new BigDecimal(amount)).intValue();
		bean.setIntegService(xbService);
		return bean;
	}
}
