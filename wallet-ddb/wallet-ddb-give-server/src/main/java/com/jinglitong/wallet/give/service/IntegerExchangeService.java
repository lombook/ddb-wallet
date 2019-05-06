package com.jinglitong.wallet.give.service;

import java.sql.Timestamp;
import java.util.List;

import com.jinglitong.wallet.give.common.storage.AliCloudMailService;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.UuidUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccountSeq;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWalletSeq;
import com.jinglitong.wallet.give.mapper.DdbCustIntegralRecordMapper;
import com.jinglitong.wallet.give.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.give.mapper.DdbIntegralAccountSeqMapper;
import com.jinglitong.wallet.give.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.give.mapper.DdbIntegralWalletSeqMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IntegerExchangeService {

	
	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;
	
	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	
	@Autowired
	private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;
	
	@Autowired
	private DdbIntegralWalletSeqMapper ddbIntegralWalletSeqMapper;
	
	@Autowired
	private DdbIntegralAccountSeqMapper ddbIntegralAccountSeqMapper;
	
	@Autowired
	private AliCloudMailService aliCloudMailService;
	
	@Value("${payAcc_email_address}")
	private String payAccEmailAddress;

	@Value("${payAcc.amount}")
	private Long payAccAmount;
	/**
	 * 
	 * 功能说明:获取股权列表
	 * @param ruleType
	 * @return
	 */
	/*public List<DdbStockExchangeRule> getStockRule(int ruleType){
		String time =DateUtils.getDateTime();
		Weekend<DdbStockExchangeRule>weekend = Weekend.of(DdbStockExchangeRule.class);
    	WeekendCriteria<DdbStockExchangeRule,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition(" rule_type ="+ruleType+"  and expiry_time >='"+time+"'");
    	List<DdbStockExchangeRule> list = ddbStockExchangeRuleMapper.selectByExample(weekend);
		return list ;
	}
	*//**
	 * 
	 * 功能说明:判断兑换的积分是否足够
	 * @param stock
	 * @param rule
	 * @return
	 *//*
	public DdbIntegralWallet isEnough(DdbStockExchangeSplit stock,DdbStockExchangeRule rule) {
		Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
    	DdbIntegralWallet wallet = new DdbIntegralWallet();
    	wallet.setCustId(customer.getCustId());
    	wallet.setInteName(rule.getIntegName());
    	wallet = ddbIntegralWalletMapper.selectOne(wallet);
    	int exAomunt = stock.getAmount() * rule.getIntegAmount();
    	if(wallet.getAmount() >= exAomunt) {
    		return wallet;
    	}
    	return null;
	}
	*//**
	 * 
	 * 功能说明:积分兑换股权
	 * @param stock
	 * @param rule
	 *//*
	@Transactional
	public void exChange(DdbStockExchangeSplit stock,DdbStockExchangeRule rule,DdbIntegralWallet wallet) {
		DdbStockExchangeSplit stock2 = new DdbStockExchangeSplit();
		stock2.setSeRuleId(stock.getSeRuleId());
		stock2.setAmount(stock.getAmount());
		//用户积分减少，回收总账增加
		DdbIntegralAccount useAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveIntegName());
		stock = builDDdbStockExchangeSplit(stock, rule,useAcc,true);
		ddbStockExchangeSplitMapper.insert(stock);
		addInteg(-stock.getAmount(), stock.getAmount(),stock.getCustId(), stock.getZid(), useAcc, "兑换股权");
		//用户股权积分增加，总账减少
		DdbIntegralAccount stockAcc= ddbIntegralAccountMapper.selectByRealName(rule.getExIntegName());
		stock2 =builDDdbStockExchangeSplit(stock2, rule,stockAcc,false);
		ddbStockExchangeSplitMapper.insert(stock2);
		addInteg(stock2.getAmount(), -stock2.getAmount(),stock2.getCustId(), stock2.getZid(), stockAcc, "兑换股权");
	}*/
	
	
	@Transactional
	public void addInteg(int walletAmount,int accAmount,String custId,String flowId, DdbIntegralAccount account,String remark) {

		//总账
		int aupdate = ddbIntegralAccountMapper.addInteger(accAmount, account.getZid());
		if(aupdate ==0){
			if(accAmount < 0 ) {
				aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + account.getZid() );
			}
			throw new RuntimeException("总账账户余额不足zid:"+account.getZid());
		}
		if(accAmount < 0 && account.getAmount() + accAmount < payAccAmount ) {
			log.info("总账余额不足"+payAccAmount+"，请注意");
			aliCloudMailService.sendMail(payAccEmailAddress, "兑换总账余额不足"+payAccAmount+"，请注意","账户不足"+payAccAmount+" zid=" + account.getZid() );
		}
		//积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),custId);
		if(ddbIntegralWallet == null){
			throw new RuntimeException("用户没有该资产Custid:"+custId+",realNmae:"+account.getIntegName());
		}
		int wupdate  = ddbIntegralWalletMapper.addInteger(walletAmount,account.getIntegName(),custId);
		if(wupdate ==0){
			throw new RuntimeException("用户账户"+account.getIntegName()+"余额不足custId:"+custId);
		}
		//记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		String dateTime = DateUtils.getDateTime();
		ddbCustIntegralRecord.setAmount(Math.abs(walletAmount));
		ddbCustIntegralRecord.setCreateTime(dateTime);
		ddbCustIntegralRecord.setUpdateTime(dateTime);
		ddbCustIntegralRecord.setCustId(custId);
		ddbCustIntegralRecord.setFlowId(flowId);
		ddbCustIntegralRecord.setIntegCname(account.getIntegCname());
		ddbCustIntegralRecord.setIntegName(account.getIntegName());
		ddbCustIntegralRecord.setRealCname(account.getRealCname());
		ddbCustIntegralRecord.setRealName(account.getRealName());
		ddbCustIntegralRecord.setInteZid(account.getZid());
		ddbCustIntegralRecord.setRemark(remark);
		if(walletAmount > 0) {
			ddbCustIntegralRecord.setType(1);
		}else {
			ddbCustIntegralRecord.setType(2);
		}
		ddbCustIntegralRecord.setZid(UuidUtil.getUUID());
		ddbCustIntegralRecordMapper.insert(ddbCustIntegralRecord);

		// 组装 新表 integral_wallet_seq
		DdbIntegralWalletSeq ddbIntegralWalletSeq = new DdbIntegralWalletSeq();
		Long beforeUserAmount = ddbIntegralWallet.getAmount();// 用户原来有多少钱
		String inteName = ddbIntegralWallet.getInteName();// 积分名
		String inteCname = ddbIntegralWallet.getInteCname();// 积分中文名
		String realName = account.getRealName();// 积分场景名
		String realCname = account.getRealCname();// 积分场景中文名
		// walletAmount 操作金额
		ddbIntegralWalletSeq.setZid(UuidUtil.getUUID());
		ddbIntegralWalletSeq.setFlowId(flowId);
		ddbIntegralWalletSeq.setCustId(custId);
		ddbIntegralWalletSeq.setRealCname(realCname);
		ddbIntegralWalletSeq.setRealName(realName);
		ddbIntegralWalletSeq.setInteCname(inteCname);
		ddbIntegralWalletSeq.setInteName(inteName);
		ddbIntegralWalletSeq.setBeforeAmount(beforeUserAmount);
		ddbIntegralWalletSeq.setAmount(Long.parseLong(String.valueOf(Math.abs(walletAmount))));// 取绝对值
		Long afterAmount = beforeUserAmount + walletAmount;
		ddbIntegralWalletSeq.setAfterAmount(afterAmount);
		if(walletAmount > 0) {
			ddbIntegralWalletSeq.setType(1);
		}else {
			ddbIntegralWalletSeq.setType(2);
		}
		
		ddbIntegralWalletSeq.setCreateTime(dateTime);
		ddbIntegralWalletSeq.setUpdateTime(dateTime);
		ddbIntegralWalletSeqMapper.insert(ddbIntegralWalletSeq);

		// 组装 新表 integral_account_seq
		DdbIntegralAccountSeq ddbIntegralAccountSeq = new DdbIntegralAccountSeq();
		ddbIntegralAccountSeq.setZid(UuidUtil.getUUID());
		ddbIntegralAccountSeq.setFlowId(flowId);
		ddbIntegralAccountSeq.setInteId(account.getZid());
		ddbIntegralAccountSeq.setRealCname(realCname);
		ddbIntegralAccountSeq.setRealName(realName);
		ddbIntegralAccountSeq.setInteCname(inteCname);
		ddbIntegralAccountSeq.setInteName(inteName);
		Long beforeamount = account.getAmount();// 总账原来有多少钱
		ddbIntegralAccountSeq.setBeforeAmount(beforeamount);
		long accountAmount = accAmount;
		ddbIntegralAccountSeq.setAmount(Math.abs(accountAmount));
		long afterAccountAmount = beforeamount + accountAmount;
		ddbIntegralAccountSeq.setAfterAmount(afterAccountAmount);
		if(accAmount > 0) {
			ddbIntegralAccountSeq.setType(1);
		}else {
			ddbIntegralAccountSeq.setType(2);
		}
		
		ddbIntegralAccountSeq.setCreateTime(dateTime);
		ddbIntegralAccountSeq.setUpdateTime(dateTime);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeq);
	}
	/**
	 * 
	 * 功能说明:构建拆单bean
	 * @param stock
	 * @param rule
	 * @return
	 */
	/*private DdbStockExchangeSplit builDDdbStockExchangeSplit(DdbStockExchangeSplit stock
			,DdbStockExchangeRule rule,DdbIntegralAccount useAcc,boolean flag) {
		Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
		stock.setZid(UuidUtil.getUUID());
		stock.setCustId(customer.getCustId());
		stock.setInteZid(useAcc.getZid());
		if(flag) {
			stock.setAmount(stock.getAmount() * rule.getIntegAmount());
		}else {
			stock.setAmount(stock.getAmount() * rule.getExAmount());
		}
		stock.setRealName(useAcc.getRealName());
		stock.setRealCname(useAcc.getRealCname());
		stock.setIntegName(useAcc.getIntegName());
		stock.setIntegCname(useAcc.getIntegCname());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		stock.setCreateTime(time);
		stock.setUpdateTime(time);
		return stock;
	}*/
}
