package com.jinglitong.wallet.ddbserver.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeOrder;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRecord;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRule;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbserver.mapper.DdbAssetAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbChainExchangeOrderMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbChainExchangeRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbChainExchangeRuleMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.SubChainMapper;
import com.jinglitong.wallet.ddbserver.mapper.WalletMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ChainExchangeServcice.java
 * 作        者: yxl 2018年11月25日
 * 创建时间: 2018年11月25日
 * 功能说明:宝分兑换MEI 
 */
@Service
@Slf4j
public class ChainExchangeServcice {

	@Autowired
	private DdbChainExchangeRuleMapper ddbChainExchangeRuleMapper; 
	
	@Autowired
	private DdbChainExchangeOrderMapper ddbChainExchangeOrderMapper;
	
	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;
	
	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	
	@Autowired
	private IntegerExchangeService integerExchangeService;
	
	@Autowired
	private DdbChainExchangeRecordMapper DdbChainExchangeRecordMapper;
	
	@Autowired
	private DdbAssetAccountMapper ddbAssetAccountMapper;
	
	@Autowired
	private WalletMapper walletMapper;
	
	@Autowired
	private SubChainMapper subChainMapper;
	
	@Value("${ddb_app_id}")
	private String appId;
	/**
	 * 
	 * 功能说明:获取兑换规则
	 * @param ruleName
	 * @return
	 */
	public DdbChainExchangeRule getChainRule(String ruleName) {
		String date = DateUtils.getDateTime();
    	Weekend<DdbChainExchangeRule>weekend = Weekend.of(DdbChainExchangeRule.class);
    	WeekendCriteria<DdbChainExchangeRule,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition(" effective_time <= '"+date+"' and expiry_time >= '"+date+"' and rule_name = '"+ruleName+"' ");
    	DdbChainExchangeRule rule =ddbChainExchangeRuleMapper.selectOneByExample(weekend);
    	rule.setMinAmount(rule.getMinAmount() / 100);
		return rule;
	}
	
	/**
	 * 
	 * 功能说明: 校验余额是否充足
	 * @param custId
	 * @param rule
	 * @param amount
	 * @return
	 */
	public Boolean getCustWallet(String custId ,DdbChainExchangeRule rule ,String amount) {
		Weekend<DdbIntegralWallet>weekend = Weekend.of(DdbIntegralWallet.class);
    	WeekendCriteria<DdbIntegralWallet,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition("cust_id = '"+custId+"' and inte_name ='"+rule.getIntegName()+"' ");
		DdbIntegralWallet wallet = ddbIntegralWalletMapper.selectOneByExample(weekend);
		int result =new BigDecimal(wallet.getAmount()).compareTo(new BigDecimal(amount).multiply(BigDecimal.valueOf(100)));
		if(result != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 功能说明:兑换申请
	 * @param custId
	 * @param rule
	 * @param amount
	 * @return
	 */
	@Transactional
	public  void exChangeApply(String custId ,DdbChainExchangeRule rule ,String amount) {
		Wallet wallet = new Wallet();
		wallet.setCustId(custId);
		wallet.setAppId(appId);
		wallet = walletMapper.selectOne(wallet);
		if(wallet == null) {
			throw new RuntimeException("没找到对应的钱包信息");
		}
		SubChain coin = new SubChain();
		coin.setCoinId(rule.getExchangeCoinId());
		coin.setAppId(appId);
		coin =  subChainMapper.selectOne(coin);
		if(coin == null) {
			throw new RuntimeException("没找到对应的币信息");
		}
		DdbChainExchangeOrder order = buildOrderBean(custId,rule, amount,wallet.getWalletId(),coin.getCurrency());
		int orderCount =ddbChainExchangeOrderMapper.insert(order);
		
		DdbIntegralAccount inteAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveIntegRealName());
		if(inteAcc == null) {
			throw new RuntimeException("没找到对应的宝分回收总账");
		}
		integerExchangeService.addInteg(-order.getIntegAmount(), order.getIntegAmount(), custId, 
				order.getZid(), inteAcc, "宝分兑换MEI",false);
		
		DdbAssetAccount asset = new DdbAssetAccount();
		asset.setState(true);
		asset.setType(1);
		asset.setCurrency(coin.getCurrency());
		asset = ddbAssetAccountMapper.selectOne(asset);
		DdbChainExchangeRecord chainRecord = buildRecordBean(order, wallet.getPublicKey(), coin.getTokenAddress(),asset.getZid() );
		int recordCount = DdbChainExchangeRecordMapper.insert(chainRecord);
		
		if(recordCount != 1 || orderCount != 1) {
			throw new RuntimeException("内部错误");
		}
	}
	 
	/**
	 * 
	 * 功能说明:构建兑换订单bean
	 * @param custId
	 * @param rule
	 * @param amount
	 * @param walletId
	 * @param currency
	 * @return
	 */
	private DdbChainExchangeOrder buildOrderBean (String custId,DdbChainExchangeRule rule,String amount,String walletId,String currency) {
		String time = DateUtils.getDateTime();
		DdbChainExchangeOrder order = new  DdbChainExchangeOrder();
		order.setZid(UuidUtil.getUUID());
		order.setExchangeRuleId(rule.getZid());
		order.setPercent(rule.getPercent());
		order.setIntegAmount(Integer.valueOf(amount) * 100);
		order.setIntegCname(rule.getIntegCname());
		order.setIntegName(rule.getIntegName());
		BigDecimal coinAmount = new BigDecimal(amount).multiply(rule.getPercent());
		order.setCoinAmount(coinAmount);
		order.setChainId(rule.getExchangeChainId());
		order.setCoinId(rule.getExchangeCoinId());
		order.setCurrency(currency);
		order.setCustId(custId);
		order.setWalletId(walletId);
		order.setState(0);
		order.setCreateTime(time);
		order.setUpdateTime(time);
		return order;
	}
	
	/**
	 * 
	 * 功能说明:构建兑换上链记录record
	 * @param order
	 * @param publicKey
	 * @param tokenAddress
	 * @param assetAcc
	 * @return
	 */
	public DdbChainExchangeRecord buildRecordBean(DdbChainExchangeOrder order,String publicKey,String tokenAddress,String assetAcc) {
		String time = DateUtils.getDateTime();
		DdbChainExchangeRecord record = new DdbChainExchangeRecord();
		record.setZid(UuidUtil.getUUID());
		record.setOrderId(order.getZid());
		record.setAssetAccountId(assetAcc);
		record.setCustId(order.getCustId());
		record.setChainId(order.getChainId());
		record.setCoinId(order.getCoinId());
		record.setCurrency(order.getCurrency());
		record.setCustPublicKey(publicKey);
		record.setCoinAmount(order.getCoinAmount());
		record.setTokenAdress(tokenAddress);
		record.setState(0);
		record.setCount(0);
		record.setCreateTime(time);
		record.setUpdateTime(time);
		return record;
	}
	
}
