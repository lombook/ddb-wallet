package com.jinglitong.wallet.give.service;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.give.common.storage.AliCloudMailService;
import com.jinglitong.wallet.give.mapper.*;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DdbIntegralAccountService {

	@Autowired
	private DdbCustIntegralWalletMapper ddbCustIntegralWalletMapper;

	@Autowired
	private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;

	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;

	@Autowired
	private RequireHistoryService requireHistoryService;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private AliCloudMailService aliCloudMailService;

	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;

	@Autowired
	private DdbIntegralWalletSeqMapper ddbIntegralWalletSeqMapper;

	@Autowired
	private DdbIntegralAccountSeqMapper ddbIntegralAccountSeqMapper;
	@Value("${payAcc_email_address}")
	private String payAccEmailAddress;

	@Value("${payAcc.amount}")
	private Long payAccAmount;


	/**
	 *
	 * 功能说明:增加宝分
	 * wallet包含三个：
	 * @param baofen 宝分
	 * @param freezBaofen 冻结宝分（可选）
	 * @param custId 用户id
	 *
	 * @param flowId 订单流水
	 * @param CostType 转账类型
	 * @param zid 总账id（可选）
	 * @return
	 */
	/*public boolean  addBf(DdbCustIntegralWallet wallet,String flowId,int CostType,String zid) {

		DdbCustIntegralRecord record =  buidRecord(wallet, flowId,CostType);
		if(!StringUtils.isEmpty(zid)) {
			DdbIntegralAccount acc = new DdbIntegralAccount();
			acc.setZid(zid);
			acc =ddbIntegralAccountMapper.selectOne(acc);
			int baofen = wallet.getBaofen()==null?wallet.getFreezBaofen():wallet.getBaofen();
			acc.setAmount(acc.getAmount() + baofen);
			int num = ddbIntegralAccountMapper.addBf(-baofen, zid);
			if(num == 0 && CostType != excAccAmount) {
				 log.info("总账余额不足,总账zid:{}",zid);
				 aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + zid );
				 throw new RuntimeException("总账账户余额不足zid:"+zid);
			}
			log.info("总账zid："+zid+"to"+"==用户id:"+wallet.getCustId()+"==amount:"+baofen+"==CostType:"+CostType);

			//判断总账金额是否小于指定金额
			if(acc.getAmount() < payAccAmount && CostType != excAccAmount) {
				log.info("总账余额不足"+payAccAmount+"，请注意");
				aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足"+payAccAmount+"，请注意","账户不足"+payAccAmount+" zid=" + zid );
			}
		}else {
			log.info("用户"+wallet.getCustId()+"amount:"+wallet.getBaofen());
		}
		wallet.setUpdateTime(DateUtils.getDateTime());
		int wall = ddbCustIntegralWalletMapper.addBf(wallet);
		int rec = ddbCustIntegralRecordMapper.insert(record);

		if(wall > 0 && rec > 0) {
			return true;
		}else {
			return false;
		}

	}

	private DdbCustIntegralRecord buidRecord(DdbCustIntegralWallet wallet,String flowId,int CostType) {
		DdbCustIntegralRecord record = new DdbCustIntegralRecord();
		//4:体现MEI,体现那边需要处理record表 zid，需要提前生成。所以这里需要把值塞进去，不能自动生成。
		if(CostType == 4) {
			record.setZid(wallet.getZid());
		}else {
			record.setZid(UuidUtil.getUUID());
		}
		record.setCustId(wallet.getCustId());
		int baofen = wallet.getBaofen()==null?wallet.getFreezBaofen():wallet.getBaofen();
		record.setBaofen(Math.abs(baofen));
		record.setCostType(CostType);//邀请人奖励
		record.setFlowId(flowId);
		record.setRemark("转账");
		record.setCreateTime(DateUtils.getDateTime());
		record.setUpdateTime(DateUtils.getDateTime());

		return record;
	}


	*//**
	 * 接收宝分
	 * @param bf
	 *//*
	public void receiveBf(Map<String, Object> bf,String date) {
        Customer customer = customerMapper.selectByCustId(bf.get("userId").toString());
        if(customer == null){
            throw new RuntimeException("用户未找到"+bf.get("userId").toString());
        }
		//排重存储
		requireHistoryService.ddbRequireHistoryInsert(bf.get("flowId").toString(),date);
        DdbCustIntegralWallet ddbCustIntegralWallet = new DdbCustIntegralWallet();
        String bfn = bf.get("meiNum")+"";
        Float num = Float.parseFloat(bfn)*10000;
        ddbCustIntegralWallet.setBaofen(num.intValue());
        ddbCustIntegralWallet.setCustId(customer.getCustId());
        addBf(ddbCustIntegralWallet,bf.get("flowId").toString(),6,null);
	}

	*//**
	 * 查询用户资产信息
	 * @return
	 *//*
	public Map<String, Object> getIntegralWallet() {
		Customer cust = (Customer)SessionUtil.getUserInfo();
		String custId = cust.getCustId();
		Map<String, Object> map=ddbCustIntegralWalletMapper.getIntegralWallet(custId);
		map.put("baofen", ((Integer)(map.get("baofen")==null ? 0 : map.get("baofen"))).doubleValue()/100+"");
		map.put("freezBaofen", ((Integer)(map.get("freezBaofen")==null ? 0:map.get("freezBaofen"))).doubleValue()/100+"");
		return JsonUtil.toJsonSuccess("获取积分成功", map);

	}*/

	/**
	 * 总账转账到用户
	 * @param amount 金额
	 * @param custId 用户id
	 * @param flowId 订单id
	 * @param account 总账
	 * @param remark 备注
	 */
	public void addInteg(int amount,String custId,String flowId, DdbIntegralAccount account,String remark) {

		if(account == null){
			throw new RuntimeException("总账账户不存在"+account.getZid());
		}else {
			//判断总账金额是否小于指定金额
			if(account.getAmount() < amount) {
				log.info("总账余额不足,总账zid:{}",account.getZid());
				aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + account.getZid() );
				throw new RuntimeException("总账账户余额不足zid:"+account.getZid());
			}
			if(account.getAmount() < payAccAmount ) {
				log.info("总账余额不足"+payAccAmount+"，请注意");
				aliCloudMailService.sendMail(payAccEmailAddress, "总账余额不足"+payAccAmount+"，请注意","账户不足"+payAccAmount+" zid=" + account.getZid() );
			}

		}
		//总账减少
		int aupdate = ddbIntegralAccountMapper.addInteger(0-amount, account.getZid());
		if(aupdate ==0){
			throw new RuntimeException("总账账户余额不足zid:"+account.getZid());
		}
		//积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),custId);
		if(ddbIntegralWallet == null){
			//新建用户钱包
			ddbIntegralWallet = new DdbIntegralWallet();
			ddbIntegralWallet.setCustId(custId);
			ddbIntegralWallet.setInteName(account.getIntegName());
			ddbIntegralWallet.setInteCname(account.getIntegCname());
			ddbIntegralWallet.setAmount(0L);
			ddbIntegralWallet.setZid(UuidUtil.getUUID());
			String dateTime = DateUtils.getDateTime();
			ddbIntegralWallet.setCreateTime(dateTime);
			ddbIntegralWallet.setUpdateTime(dateTime);
			if(ddbIntegralWalletMapper.insert(ddbIntegralWallet) < 1){
				throw new RuntimeException("用户没有该资产Custid:"+custId+",realNmae:"+account.getIntegName());
			}
			
		}
		int wupdate  = ddbIntegralWalletMapper.addInteger(amount,account.getIntegName(),custId);
		if(wupdate ==0){
			throw new RuntimeException("总账账户余额不足zid:"+account.getZid());
		}
		//记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		String dateTime = DateUtils.getDateTime();
		ddbCustIntegralRecord.setAmount(amount);
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
		ddbCustIntegralRecord.setZid(UuidUtil.getUUID());
		ddbCustIntegralRecord.setType(1);
		ddbCustIntegralRecordMapper.insert(ddbCustIntegralRecord);
		DdbIntegralWalletSeq ddbIntegralWalletSeq = new DdbIntegralWalletSeq(UuidUtil.getUUID(),custId,account.getRealCname(),account.getRealName(),account.getIntegCname(),account.getIntegName(),
				ddbIntegralWallet.getAmount(),Long.parseLong(String.valueOf(amount)),ddbIntegralWallet.getAmount()+Long.parseLong(String.valueOf(amount)),1,dateTime,dateTime,flowId);
		ddbIntegralWalletSeqMapper.insert(ddbIntegralWalletSeq);
		DdbIntegralAccountSeq ddbIntegralAccountSeq = new DdbIntegralAccountSeq(UuidUtil.getUUID(),flowId,account.getZid(),account.getRealCname(),account.getRealName(),account.getIntegCname(),
				account.getIntegName(),account.getAmount(),Long.parseLong(amount+""),account.getAmount()-amount,2,dateTime,dateTime);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeq);
	}


}
