package com.jinglitong.wallet.ddbserver.service;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudMailService;
import com.jinglitong.wallet.ddbserver.mapper.*;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DdbIntegralAccountService {


	@Autowired
	private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;

	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;

	@Autowired
	private AliCloudMailService aliCloudMailService;

	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;

	@Autowired
	private DdbIntegralWalletSeqMapper ddbIntegralWalletSeqMapper;

	@Autowired
	private DdbIntegralAccountSeqMapper ddbIntegralAccountSeqMapper;

	@Autowired
	private DdbIntegralSubAccountTransferMapper ddbIntegralSubAccountTransferMapper;

	@Value("${payAcc_email_address}")
	private String payAccEmailAddress;

	@Value("${payAcc.amount}")
	private Long payAccAmount;




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
			throw new RuntimeException("用户没有该资产Custid:"+custId+",realNmae:"+account.getIntegName());
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

	/**
	 *
	 * @param amount addRealNmae 增加金额
	 * @param addRealNmae 增加的总账名
	 * @param minRealName 减少总账积分名
     * @Param flowId 业务Id
	 */
	public void accountToAccount(Integer amount,String addRealNmae,String minRealName,String flowId,String remark){
	    String date = DateUtils.getDateTime();
        DdbIntegralAccount addAccount = ddbIntegralAccountMapper.selectByRealName(addRealNmae);
        DdbIntegralAccount minAccount = ddbIntegralAccountMapper.selectByRealName(minRealName);
        if(addAccount == null || minAccount == null){
            throw new RuntimeException("总账不存在");
        }
        //总账增加
		int update = ddbIntegralAccountMapper.addInteger(amount, addAccount.getZid());
        if(update == 0){
        	throw new RuntimeException("转账失败，检查数据。realName:"+addAccount.getRealName());
		}
		//总账减少
		update = ddbIntegralAccountMapper.addInteger(0 - amount, minAccount.getZid());
		if(update == 0){
			throw new RuntimeException("转账失败，检查数据。realName:"+addAccount.getRealName());
		}
		//记录record
        DdbIntegralSubAccountTransfer ddbIntegralSubAccountTransfer = new DdbIntegralSubAccountTransfer();
        ddbIntegralSubAccountTransfer.setAmount(Long.parseLong(amount.toString()));
        ddbIntegralSubAccountTransfer.setCreateTime(date);
        ddbIntegralSubAccountTransfer.setFlowId(flowId);
        ddbIntegralSubAccountTransfer.setZid(UuidUtil.getUUID());
        ddbIntegralSubAccountTransfer.setIntegCname(addAccount.getIntegCname());
        ddbIntegralSubAccountTransfer.setIntegName(addAccount.getIntegName());
        ddbIntegralSubAccountTransfer.setRealCname(addAccount.getRealCname());
        ddbIntegralSubAccountTransfer.setRealName(addAccount.getRealName());
        ddbIntegralSubAccountTransfer.setInteZid(addAccount.getZid());
        ddbIntegralSubAccountTransfer.setRemark(remark);
        ddbIntegralSubAccountTransfer.setType(1);
        ddbIntegralSubAccountTransfer.setUpdateTime(date);
        ddbIntegralSubAccountTransferMapper.insert(ddbIntegralSubAccountTransfer);
        ddbIntegralSubAccountTransfer.setZid(UuidUtil.getUUID());
        ddbIntegralSubAccountTransfer.setRealName(minAccount.getRealName());
        ddbIntegralSubAccountTransfer.setRealCname(minAccount.getRealCname());
        ddbIntegralSubAccountTransfer.setIntegName(minAccount.getIntegName());
        ddbIntegralSubAccountTransfer.setIntegCname(minAccount.getIntegCname());
        ddbIntegralSubAccountTransfer.setType(2);
        //总账增加记录
        ddbIntegralSubAccountTransferMapper.insert(ddbIntegralSubAccountTransfer);
		DdbIntegralAccountSeq ddbIntegralAccountSeqadd = new DdbIntegralAccountSeq(UuidUtil.getUUID(),flowId,addAccount.getZid(),addAccount.getRealCname(),addAccount.getRealName(),addAccount.getIntegCname(),
				addAccount.getIntegName(),addAccount.getAmount(),Long.parseLong(amount+""),addAccount.getAmount()+amount,1,date,date);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeqadd);
		//临时总账减少记录
		DdbIntegralAccountSeq ddbIntegralAccountSeqmin = new DdbIntegralAccountSeq(UuidUtil.getUUID(),flowId,minAccount.getZid(),minAccount.getRealCname(),minAccount.getRealName(),minAccount.getIntegCname(),
				minAccount.getIntegName(),minAccount.getAmount(),Long.parseLong(amount+""),minAccount.getAmount()-amount,2,date,date);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeqmin);
    }

}
