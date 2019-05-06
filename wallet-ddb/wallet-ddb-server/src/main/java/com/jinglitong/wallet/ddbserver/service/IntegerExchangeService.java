package com.jinglitong.wallet.ddbserver.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegExchangeRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegExchangeRule;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccountSeq;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWalletSeq;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;
import com.jinglitong.wallet.ddbapi.model.view.ChainExchangeVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegExchangeRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRecordVo;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudMailService;
import com.jinglitong.wallet.ddbserver.mapper.DdbCustIntegralRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegExchangeRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegExchangeRuleMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountSeqMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletSeqMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Slf4j
public class IntegerExchangeService {

	@Autowired
	private DdbIntegExchangeRuleMapper ddbStockExchangeRuleMapper;
	
	@Autowired
	private DdbIntegExchangeRecordMapper ddbStockExchangeSplitMapper;
	
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
	public List<DdbIntegExchangeRule> getExRule(String exName){
		String time =DateUtils.getDateTime();
		Weekend<DdbIntegExchangeRule>weekend = Weekend.of(DdbIntegExchangeRule.class);
    	WeekendCriteria<DdbIntegExchangeRule,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition(" ex_name ='"+exName+"'  and expiry_time >='"+time+"' and effective_time <= '"+time+"' ");
    	List<DdbIntegExchangeRule> list = ddbStockExchangeRuleMapper.selectByExample(weekend);
    	if(list.size() != 0) {
    		for (DdbIntegExchangeRule ddbIntegExchangeRule : list) {
    			ddbIntegExchangeRule.setMinAmount(ddbIntegExchangeRule.getMinAmount() / 100);
			}
    	}
		return list ;
	}
	/**
	 * 
	 * 功能说明:判断兑换的积分是否足够
	 * @param stock
	 * @param rule
	 * @return
	 */
	public DdbIntegralWallet isEnough(ChainExchangeVo vo,DdbIntegExchangeRule rule) {
		Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
		//customer.setCustId("ffe14deec12a486c9a53b6ca90527f69");
    	DdbIntegralWallet wallet = new DdbIntegralWallet();
    	wallet.setCustId(customer.getCustId());
    	wallet.setInteName(rule.getIntegName());
    	wallet = ddbIntegralWalletMapper.selectOne(wallet);
    	int exAomunt = Integer.parseInt(vo.getAmount()) * rule.getIntegPercent().intValue();
    	if(wallet.getAmount() >= exAomunt) {
    		return wallet;
    	}
    	return null;
	}
	/**
	 * 
	 * 功能说明:积分兑换股权
	 * @param stock
	 * @param rule
	 */
	@Transactional
	public void exChange(ChainExchangeVo vo,DdbIntegExchangeRule rule,DdbIntegralWallet wallet) {
		int amount =Integer.parseInt(vo.getAmount()) * 100;
		int gqAmount = amount * rule.getIntegPercent().intValue();
		//用户积分减少，回收总账增加
		DdbIntegralAccount useAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveAccName());
		DdbIntegExchangeRecord stock   = builDDdbStockExchangeSplit(Integer.parseInt(vo.getAmount()),gqAmount,rule,useAcc,true);
		stock.setType(2);
		ddbStockExchangeSplitMapper.insert(stock);
		
		addInteg(-amount, amount,stock.getCustId(), stock.getZid(), useAcc, "兑换股权",true);
		//用户股权积分增加，总账减少
		
		DdbIntegralAccount stockAcc= ddbIntegralAccountMapper.selectByRealName(rule.getExIntegName());
		addInteg(gqAmount, -gqAmount,stock.getCustId(), stock.getZid(), stockAcc, "兑换股权",true);
	}
	
	
	@Transactional
	public void addInteg(int walletAmount,int accAmount,String custId,String flowId, DdbIntegralAccount account,String remark,Boolean sendMail) {

		//总账
		int aupdate = ddbIntegralAccountMapper.addInteger(accAmount, account.getZid());
		if(aupdate ==0){
			if(accAmount < 0 ) {
				aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + account.getZid() );
			}
			throw new RuntimeException("总账账户余额不足zid:"+account.getZid());
		}
		if(sendMail){
			if(accAmount < 0 && account.getAmount() + accAmount < payAccAmount ) {
				log.info("总账余额不足"+payAccAmount+"，请注意");
				aliCloudMailService.sendMail(payAccEmailAddress, "兑换总账余额不足"+payAccAmount+"，请注意","账户不足"+payAccAmount+" zid=" + account.getZid() );
			}
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
		ddbIntegralWalletSeq.setAfterAmount((Long)afterAmount);
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
	private DdbIntegExchangeRecord builDDdbStockExchangeSplit(int amount ,int exAmount, DdbIntegExchangeRule rule,DdbIntegralAccount useAcc,boolean flag) {
		DdbIntegExchangeRecord stock = new DdbIntegExchangeRecord();
		stock.setIeRuleId(rule.getZid());
		Customer customer = ((Customer) SecurityUtils.getSubject().getPrincipal());
		//customer.setCustId("ffe14deec12a486c9a53b6ca90527f69");
		stock.setZid(UuidUtil.getUUID());
		stock.setCustId(customer.getCustId());
		stock.setInteZid(useAcc.getZid());
		if(flag) {
			stock.setAmount(amount * 100 * rule.getIntegPercent().intValue());
		}else {
			stock.setAmount(amount * 100 * rule.getIntegPercent().intValue());
		}
		stock.setRealName(useAcc.getRealName());
		stock.setRealCname(useAcc.getRealCname());
		stock.setIntegName(useAcc.getIntegName());
		stock.setIntegCname(useAcc.getIntegCname());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		stock.setCreateTime(time);
		stock.setUpdateTime(time);
		stock.setExAmount(exAmount);
		stock.setExIntegCname(rule.getExIntegCname());
		stock.setExIntegName(rule.getExIntegName());
		stock.setChargeIntegCname(rule.getChargeIntegCname());
		stock.setChargeIntegName(rule.getChargeIntegName());
		return stock;
	}
	
	
	
	
	/*****************************************************金贝兑换现贝**************************************************************/
	
	public Boolean InteIsEnough(String custId,String jb,Integer bf,DdbIntegExchangeRule rule){
		Weekend<DdbIntegralWallet>weekend = Weekend.of(DdbIntegralWallet.class);
    	WeekendCriteria<DdbIntegralWallet,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition("cust_id = '"+custId+"' and (inte_name ='"+rule.getIntegName()+"' or inte_name = '"+rule.getChargeIntegName()+"')");
		List<DdbIntegralWallet> list = ddbIntegralWalletMapper.selectByExample(weekend);
		BigDecimal xbAmount = new BigDecimal(jb).multiply(new BigDecimal(100));
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
	@Transactional
	public void jinExchange(ChainExchangeVo vo,DdbIntegExchangeRule rule) {
		int exAmount =Integer.parseInt(vo.getAmount()) * 100 * rule.getIntegPercent().intValue();
		int chargeAmount = Integer.parseInt(vo.getAmount()) * 100 * rule.getChargePercent().intValue();
		DdbIntegralAccount useAcc= ddbIntegralAccountMapper.selectByRealName(rule.getResaveAccName());
		DdbIntegExchangeRecord inRecord = builDDdbStockExchangeSplit(Integer.parseInt(vo.getAmount()),exAmount,rule,useAcc,true);
		inRecord.setAmount(Integer.parseInt(vo.getAmount()) * 100);
		addInteg(-inRecord.getAmount(), inRecord.getAmount(),inRecord.getCustId(), inRecord.getZid(), useAcc, "金贝兑换现贝",true);
		inRecord.setType(1);
		inRecord.setChargeAmount(chargeAmount);
		ddbStockExchangeSplitMapper.insert(inRecord);
		
		DdbIntegralAccount exAcc= ddbIntegralAccountMapper.selectByRealName(rule.getExAccName());
		addInteg(exAmount, -exAmount,inRecord.getCustId(), inRecord.getZid(), exAcc, "金贝兑换现贝",true);
		
		
		DdbIntegralAccount chargeAcc= ddbIntegralAccountMapper.selectByRealName(rule.getChargeAccName());
		addInteg(-chargeAmount, chargeAmount,inRecord.getCustId(),inRecord.getZid(), chargeAcc, "金贝兑换现贝",true);
	}
	
	public Map getRecord(DdbIntegExchangeRecordVo vo){
		Customer cust = (Customer) SessionUtil.getUserInfo();
		//cust.setCustId("ffe14deec12a486c9a53b6ca90527f69");
		if (vo.getPage() != null && vo.getRows() != null) {
			PageHelper.startPage(vo.getPage(), vo.getRows());
		}
		vo.setCustId(cust.getCustId());
		List<DdbIntegExchangeRecord> list = ddbStockExchangeSplitMapper.getRecord(vo);
		PageInfo pageinfo = new PageInfo<>(list);
		HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("list",list);
		return map;
	}
}
