package com.jinglitong.wallet.ddbkjserver.service;

import java.util.Date;
import java.util.List;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbkjserver.util.DateUtils;
import com.jinglitong.wallet.ddbkjserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Slf4j
public class IntegerExchangeService {

	@Autowired
	private DdbOrderMapper ddbOrderMapper;

	@Autowired
	private DdbSeplitOrderRewardMapper ddbSeplitOrderRewardMapper;

	@Autowired
	private DdbSeplitOrderRewardGfMapper ddbSeplitOrderRewardGfMapper;

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

	/**
	 * 转账处理，回填DB，更新订单状态
	 * 
	 * 查询拆单表，组装转账信息 。 调用yxl转账，实现： 加减总账， 加减个人账户 ，添加转账记录。
	 * 
	 * 然后，ltf添加 ，更新订单表，拆单表状态
	 * 
	 * @param order
	 */

	@Transactional
	public void exChange(KJNotice order) {

		// 1.查询拆单表,获得需要处理的订单 条件 state=0 未处理
		DdbSeplitOrderReward ddbSeplitOrderReward = new DdbSeplitOrderReward();
		ddbSeplitOrderReward.setState(0);
		ddbSeplitOrderReward.setFlowId(order.getFlowId());
		List<DdbSeplitOrderReward> ddbSeplitOrderRewardsList = ddbSeplitOrderRewardMapper.select(ddbSeplitOrderReward);

		// 遍历拆单表 根据拆单记录 组装转账信息
		for (DdbSeplitOrderReward splitOrderReward : ddbSeplitOrderRewardsList) {

			// 积分场景类型
			String realName = splitOrderReward.getRealName();

			// 用户的金额 , 总账的金额 ,用户ID, flow_id,总账 remark

			// 金额 需要根据 场景类型 判断正负。
			// 目前业务： 都是用户账户增加 总账减少
			Integer amount = splitOrderReward.getAmount();
			// flow_id
			String flowId = splitOrderReward.getFlowId();

			// 添加判断，如果金额为0，不做转账，但是仍然修改订单表的状态
			if (amount != 0) {
				// 订单 用户账户增加 总账减少。
				// if("sb_fenqi".equals(realName) || "sb_dingdan".equals(realName) ||
				// "mb_dingdan".equals(realName)) {
				//
				// }

				// 用户ID
				String custId = splitOrderReward.getCustId();

				// 总账
				DdbIntegralAccount useAcc = ddbIntegralAccountMapper.selectByRealName(realName);
				// remark
				String remark = splitOrderReward.getRealCname();
				// 2.调用转账方法，实现：加减总账， 加减个人账户 ，添加转账记录
				// 目前业务： 都是用户账户增加 总账减少
				// addInteg(amount, -amount, custId, flowId, useAcc, remark);
				addInteg(amount, -amount, useAcc, splitOrderReward);// 修改 yxl转账方法 将几个参数 的传递 合并为一个对象的传递
			}

			// 3.更改订单状态
//			ddbOrderMapper.updateStateByFlowId(1, flowId);
			// 4.更改 拆单表状态
			ddbSeplitOrderRewardMapper.updateStateByzId(1, splitOrderReward.getZid());

		}

	}


	/**
	 * 转账处理，回填DB，更新订单状态
	 *
	 * 查询拆单表，组装转账信息 。 调用yxl转账，实现： 加减总账， 加减个人账户 ，添加转账记录。
	 *
	 * 然后，ltf添加 ，更新订单表，拆单表状态
	 *
	 * @param order
	 */

	@Transactional
	public void exChange2(KJNotice order) {

		// 1.查询拆单表,获得需要处理的订单 条件 state=0 未处理
		DdbSeplitOrderRewardGf ddbSeplitOrderRewardGf = new DdbSeplitOrderRewardGf();
		ddbSeplitOrderRewardGf.setState(0);
		ddbSeplitOrderRewardGf.setFlowId(order.getFlowId());
		List<DdbSeplitOrderRewardGf> ddbSeplitOrderRewardsList = ddbSeplitOrderRewardGfMapper.select(ddbSeplitOrderRewardGf);

		// 遍历拆单表 根据拆单记录 组装转账信息
		for (DdbSeplitOrderRewardGf splitOrderReward : ddbSeplitOrderRewardsList) {

			// 积分场景类型
			String realName = splitOrderReward.getRealName();

			// 用户的金额 , 总账的金额 ,用户ID, flow_id,总账 remark

			// 金额 需要根据 场景类型 判断正负。
			// 目前业务： 都是用户账户增加 总账减少
			Integer amount = splitOrderReward.getAmount();
			// flow_id
			String flowId = splitOrderReward.getFlowId();

			// 添加判断，如果金额为0，不做转账，但是仍然修改订单表的状态
			if (amount != 0) {
				// 订单 用户账户增加 总账减少。
				// if("sb_fenqi".equals(realName) || "sb_dingdan".equals(realName) ||
				// "mb_dingdan".equals(realName)) {
				//
				// }

				// 用户ID
				String custId = splitOrderReward.getCustId();

				// 总账
				DdbIntegralAccount useAcc = ddbIntegralAccountMapper.selectByRealName(realName);
				// remark
				String remark = splitOrderReward.getRealCname();
				// 2.调用转账方法，实现：加减总账， 加减个人账户 ，添加转账记录
				// 目前业务： 都是用户账户增加 总账减少
				// addInteg(amount, -amount, custId, flowId, useAcc, remark);
				addInteg(amount, -amount, useAcc, splitOrderReward);// 修改 yxl转账方法 将几个参数 的传递 合并为一个对象的传递
			}

			// 3.更改订单状态
//			ddbOrderMapper.updateStateByFlowId(1, flowId);
			// 4.更改 拆单表状态
			ddbSeplitOrderRewardGfMapper.updateStateByzId(1, splitOrderReward.getZid());

		}

	}

	/**
	 * copy from yxl转账 功能： 加减总账， 加减个人账户 ，添加转账记录
	 * 
	 * @param walletAmount
	 *            用户的金额
	 * @param accAmount
	 *            总账的金额
	 * @param custId
	 *            用户ID
	 * @param flowId
	 *            flow_id
	 * @param account
	 *            总账
	 * @param remark
	 *            remark信息 对应拆单表的real_cname
	 */
	@Transactional
	public void addInteg(int walletAmount, int accAmount, String custId, String flowId, DdbIntegralAccount account,
			String remark) {

		// 总账减少
		int aupdate = ddbIntegralAccountMapper.addInteger(accAmount, account.getZid());
		if (aupdate == 0) {
			throw new RuntimeException("总账账户余额不足zid:" + account.getZid());
		}
		// 积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),
				custId);
		if (ddbIntegralWallet == null) {
			throw new RuntimeException("用户没有该资产Custid:" + custId + ",realNmae:" + account.getIntegName());
		}
		int wupdate = ddbIntegralWalletMapper.addInteger(walletAmount, account.getIntegName(), custId);
		if (wupdate == 0) {
			throw new RuntimeException("用户账户" + account.getIntegName() + "余额不足custId:" + custId);
		}
		// 记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		String dateTime = DateUtils.getDateTime();
		ddbCustIntegralRecord.setAmount(walletAmount);
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
		ddbCustIntegralRecordMapper.insert(ddbCustIntegralRecord);
	}

	@Transactional
	public void addInteg(int walletAmount, int accAmount, DdbIntegralAccount account,
			DdbSeplitOrderReward ddbSeplitOrderReward) {

		// 总账减少
		int aupdate = ddbIntegralAccountMapper.addInteger(accAmount, account.getZid());
		if (aupdate == 0) {
			throw new RuntimeException("总账账户余额不足zid:" + account.getZid());
		}
		// 积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),
				ddbSeplitOrderReward.getCustId());
		if(ddbIntegralWallet==null){
			ddbIntegralWallet =addIntegralWallet(ddbIntegralWallet,ddbSeplitOrderReward.getCustId(),account.getIntegName(),account.getIntegCname());
			if(ddbIntegralWallet==null) {
				throw new RuntimeException("用户没有该资产Custid:" + ddbSeplitOrderReward.getCustId() + ",realNmae:" + account.getIntegName());
			}
		}
		int wupdate = ddbIntegralWalletMapper.addInteger(walletAmount, account.getIntegName(),
				ddbSeplitOrderReward.getCustId());
		if (wupdate == 0) {
			throw new RuntimeException(
					"用户账户" + account.getIntegName() + "余额不足custId:" + ddbSeplitOrderReward.getCustId());
		}
		// 根据fyy稽核审查的需求，三张记录表的时间一致， 所以创建一个公用时间
//		Date date = new Date();
//		String formatDateTime = DateUtils.formatDateTime(date);
		String formatDateTime = DateUtils.getDateTime();
		
		// 记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
//		String dateTime = DateUtils.getDateTime();
		ddbCustIntegralRecord.setAmount(walletAmount);
		ddbCustIntegralRecord.setCreateTime(formatDateTime);
		ddbCustIntegralRecord.setUpdateTime(formatDateTime);
		ddbCustIntegralRecord.setCustId(ddbSeplitOrderReward.getCustId());
		ddbCustIntegralRecord.setFlowId(ddbSeplitOrderReward.getFlowId());
		ddbCustIntegralRecord.setIntegCname(account.getIntegCname());
		ddbCustIntegralRecord.setIntegName(account.getIntegName());
		ddbCustIntegralRecord.setRealCname(account.getRealCname());
		ddbCustIntegralRecord.setRealName(account.getRealName());
		ddbCustIntegralRecord.setInteZid(account.getZid());
		ddbCustIntegralRecord.setRemark(ddbSeplitOrderReward.getRealCname());
		ddbCustIntegralRecord.setZid(UuidUtil.getUUID());
		ddbCustIntegralRecord.setType(1);// 目前都是增加
		ddbCustIntegralRecordMapper.insert(ddbCustIntegralRecord);

		// 组装 新表 integral_wallet_seq
		DdbIntegralWalletSeq ddbIntegralWalletSeq = new DdbIntegralWalletSeq();
		Long beforeUserAmount = ddbIntegralWallet.getAmount();// 用户原来有多少钱
		String inteName = ddbIntegralWallet.getInteName();// 积分名
		String inteCname = ddbIntegralWallet.getInteCname();// 积分中文名
		String realName = ddbSeplitOrderReward.getRealName();// 积分场景名
		String realCname = ddbSeplitOrderReward.getRealCname();// 积分场景中文名
		// walletAmount 操作金额
		ddbIntegralWalletSeq.setZid(UuidUtil.getUUID());
		ddbIntegralWalletSeq.setFlowId(ddbSeplitOrderReward.getFlowId());
		ddbIntegralWalletSeq.setCustId(ddbSeplitOrderReward.getCustId());
		ddbIntegralWalletSeq.setRealCname(realCname);
		ddbIntegralWalletSeq.setRealName(realName);
		ddbIntegralWalletSeq.setInteCname(inteCname);
		ddbIntegralWalletSeq.setInteName(inteName);
		ddbIntegralWalletSeq.setBeforeAmount(beforeUserAmount);
		ddbIntegralWalletSeq.setAmount(Long.parseLong(String.valueOf(Math.abs(walletAmount))));// 取绝对值
		Long afterAmount = beforeUserAmount + walletAmount;
		ddbIntegralWalletSeq.setAfterAmount(afterAmount);
		// type 目前都是增加 1
		ddbIntegralWalletSeq.setType(1);
		ddbIntegralWalletSeq.setCreateTime(formatDateTime);
		ddbIntegralWalletSeq.setUpdateTime(formatDateTime);
		ddbIntegralWalletSeqMapper.insert(ddbIntegralWalletSeq);
		
		// 组装 新表 integral_account_seq
		DdbIntegralAccountSeq ddbIntegralAccountSeq = new DdbIntegralAccountSeq();
		ddbIntegralAccountSeq.setZid(UuidUtil.getUUID());
		ddbIntegralAccountSeq.setFlowId(ddbSeplitOrderReward.getFlowId());
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
		// type 目前都是减少 2
		ddbIntegralAccountSeq.setType(2);
		ddbIntegralAccountSeq.setCreateTime(formatDateTime);
		ddbIntegralAccountSeq.setUpdateTime(formatDateTime);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeq);
	}


	@Transactional
	public void addInteg(int walletAmount, int accAmount, DdbIntegralAccount account,
						 DdbSeplitOrderRewardGf ddbSeplitOrderRewardGf) {

		// 总账减少
		int aupdate = ddbIntegralAccountMapper.addInteger(accAmount, account.getZid());
		if (aupdate == 0) {
			throw new RuntimeException("总账账户余额不足zid:" + account.getZid());
		}
		// 积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),
				ddbSeplitOrderRewardGf.getCustId());
		if(ddbIntegralWallet==null){
			ddbIntegralWallet =addIntegralWallet(ddbIntegralWallet,ddbSeplitOrderRewardGf.getCustId(),account.getIntegName(),account.getIntegCname());
			if(ddbIntegralWallet==null) {
				throw new RuntimeException("用户没有该资产Custid:" + ddbSeplitOrderRewardGf.getCustId() + ",realNmae:" + account.getIntegName());
			}
		}
		int wupdate = ddbIntegralWalletMapper.addInteger(walletAmount, account.getIntegName(),
				ddbSeplitOrderRewardGf.getCustId());
		if (wupdate == 0) {
			throw new RuntimeException(
					"用户账户" + account.getIntegName() + "余额不足custId:" + ddbSeplitOrderRewardGf.getCustId());
		}
		// 根据fyy稽核审查的需求，三张记录表的时间一致， 所以创建一个公用时间
//		Date date = new Date();
//		String formatDateTime = DateUtils.formatDateTime(date);
		String formatDateTime = DateUtils.getDateTime();

		// 记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
//		String dateTime = DateUtils.getDateTime();
		ddbCustIntegralRecord.setAmount(walletAmount);
		ddbCustIntegralRecord.setCreateTime(formatDateTime);
		ddbCustIntegralRecord.setUpdateTime(formatDateTime);
		ddbCustIntegralRecord.setCustId(ddbSeplitOrderRewardGf.getCustId());
		ddbCustIntegralRecord.setFlowId(ddbSeplitOrderRewardGf.getFlowId());
		ddbCustIntegralRecord.setIntegCname(account.getIntegCname());
		ddbCustIntegralRecord.setIntegName(account.getIntegName());
		ddbCustIntegralRecord.setRealCname(account.getRealCname());
		ddbCustIntegralRecord.setRealName(account.getRealName());
		ddbCustIntegralRecord.setInteZid(account.getZid());
		ddbCustIntegralRecord.setRemark(ddbSeplitOrderRewardGf.getRealCname());
		ddbCustIntegralRecord.setZid(UuidUtil.getUUID());
		ddbCustIntegralRecord.setType(1);// 目前都是增加
		ddbCustIntegralRecordMapper.insert(ddbCustIntegralRecord);

		// 组装 新表 integral_wallet_seq
		DdbIntegralWalletSeq ddbIntegralWalletSeq = new DdbIntegralWalletSeq();
		Long beforeUserAmount = ddbIntegralWallet.getAmount();// 用户原来有多少钱
		String inteName = ddbIntegralWallet.getInteName();// 积分名
		String inteCname = ddbIntegralWallet.getInteCname();// 积分中文名
		String realName = ddbSeplitOrderRewardGf.getRealName();// 积分场景名
		String realCname = ddbSeplitOrderRewardGf.getRealCname();// 积分场景中文名
		// walletAmount 操作金额
		ddbIntegralWalletSeq.setZid(UuidUtil.getUUID());
		ddbIntegralWalletSeq.setFlowId(ddbSeplitOrderRewardGf.getFlowId());
		ddbIntegralWalletSeq.setCustId(ddbSeplitOrderRewardGf.getCustId());
		ddbIntegralWalletSeq.setRealCname(realCname);
		ddbIntegralWalletSeq.setRealName(realName);
		ddbIntegralWalletSeq.setInteCname(inteCname);
		ddbIntegralWalletSeq.setInteName(inteName);
		ddbIntegralWalletSeq.setBeforeAmount(beforeUserAmount);
		ddbIntegralWalletSeq.setAmount(Long.parseLong(String.valueOf(Math.abs(walletAmount))));// 取绝对值
		Long afterAmount = beforeUserAmount + walletAmount;
		ddbIntegralWalletSeq.setAfterAmount(afterAmount);
		// type 目前都是增加 1
		ddbIntegralWalletSeq.setType(1);
		ddbIntegralWalletSeq.setCreateTime(formatDateTime);
		ddbIntegralWalletSeq.setUpdateTime(formatDateTime);
		ddbIntegralWalletSeqMapper.insert(ddbIntegralWalletSeq);

		// 组装 新表 integral_account_seq
		DdbIntegralAccountSeq ddbIntegralAccountSeq = new DdbIntegralAccountSeq();
		ddbIntegralAccountSeq.setZid(UuidUtil.getUUID());
		ddbIntegralAccountSeq.setFlowId(ddbSeplitOrderRewardGf.getFlowId());
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
		// type 目前都是减少 2
		ddbIntegralAccountSeq.setType(2);
		ddbIntegralAccountSeq.setCreateTime(formatDateTime);
		ddbIntegralAccountSeq.setUpdateTime(formatDateTime);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeq);
	}

	/**
	 * 总账给个人转账
	 * @param transferNum 转账数量
	 * @param custId  用户Id
	 * @param inteId  转的积分Id
	 * @param flowId 订单流水号
	 */
	@Transactional
	public void transferAccounts(int transferNum, String custId, String  inteId,String flowId) {
		Weekend<DdbIntegralAccount> weekendAccount = Weekend.of(DdbIntegralAccount.class);
	    WeekendCriteria<DdbIntegralAccount, Object> criteriaAccount = weekendAccount.weekendCriteria();
	    criteriaAccount.andEqualTo(DdbIntegralAccount::getZid,inteId);  
	    DdbIntegralAccount account = ddbIntegralAccountMapper.selectOneByExample(weekendAccount);	   
	    if(account == null){
	    	throw new RuntimeException("总账账户不存在" + inteId);
	    }

		// 总账减少	    
		int update = ddbIntegralAccountMapper.addInteger(-transferNum, account.getZid());
		if (update == 0) {
			throw new RuntimeException("总账账户余额不足zid:" + account.getZid());
		}

		// 积分增加
		DdbIntegralWallet ddbIntegralWallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(account.getIntegName(),custId);
		if (ddbIntegralWallet == null) {
			//新建用户钱包
			ddbIntegralWallet = addIntegralWallet(ddbIntegralWallet,custId,account.getIntegName(),account.getIntegCname());
			if(ddbIntegralWallet==null){
				throw new RuntimeException("用户没有该资产Custid:"+custId+",realNmae:"+account.getIntegName());
			}
		}
		int wupdate = ddbIntegralWalletMapper.addInteger(transferNum, account.getIntegName(),custId);
		if (wupdate == 0) {
			throw new RuntimeException(
					"用户账户" + account.getIntegName() + "余额不足custId:" + custId);
		}

		String formatDateTime = DateUtils.getDateTime();
		
		// 记录日志
		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		ddbCustIntegralRecord.setAmount(transferNum);
		ddbCustIntegralRecord.setCreateTime(formatDateTime);
		ddbCustIntegralRecord.setUpdateTime(formatDateTime);
		ddbCustIntegralRecord.setCustId(custId);
		ddbCustIntegralRecord.setFlowId(flowId);
		ddbCustIntegralRecord.setIntegCname(account.getIntegCname());
		ddbCustIntegralRecord.setIntegName(account.getIntegName());
		ddbCustIntegralRecord.setRealCname(account.getRealCname());
		ddbCustIntegralRecord.setRealName(account.getRealName());
		ddbCustIntegralRecord.setInteZid(account.getZid());
		ddbCustIntegralRecord.setRemark(account.getRealCname());
		ddbCustIntegralRecord.setZid(UuidUtil.getUUID());
		ddbCustIntegralRecord.setType(1);// 目前都是增加
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
		ddbIntegralWalletSeq.setAmount(Long.parseLong(String.valueOf(Math.abs(transferNum))));// 取绝对值
		Long afterAmount = beforeUserAmount + transferNum;
		ddbIntegralWalletSeq.setAfterAmount(afterAmount);
		// type 目前都是增加 1
		ddbIntegralWalletSeq.setType(1);
		ddbIntegralWalletSeq.setCreateTime(formatDateTime);
		ddbIntegralWalletSeq.setUpdateTime(formatDateTime);
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
		long accountAmount = - transferNum;
		ddbIntegralAccountSeq.setAmount(Math.abs(accountAmount));
		long afterAccountAmount = beforeamount + accountAmount;
		ddbIntegralAccountSeq.setAfterAmount(afterAccountAmount);
		// type 目前都是减少 2
		ddbIntegralAccountSeq.setType(2);
		ddbIntegralAccountSeq.setCreateTime(formatDateTime);
		ddbIntegralAccountSeq.setUpdateTime(formatDateTime);
		ddbIntegralAccountSeqMapper.insert(ddbIntegralAccountSeq);
	}

	/**
	 * 增加新币种
	 * @param ddbIntegralWallet
	 * @param custId
	 * @param inteName
	 * @param integCname
	 * @return
	 */
	private DdbIntegralWallet addIntegralWallet(DdbIntegralWallet ddbIntegralWallet,String custId,String inteName,String integCname){
		//新建用户钱包
		ddbIntegralWallet = new DdbIntegralWallet();
		ddbIntegralWallet.setCustId(custId);
		ddbIntegralWallet.setInteName(inteName);
		ddbIntegralWallet.setInteCname(integCname);
		ddbIntegralWallet.setAmount(0L);
		ddbIntegralWallet.setZid(UuidUtil.getUUID());
		String dateTime = DateUtils.getDateTime();
		ddbIntegralWallet.setCreateTime(dateTime);
		ddbIntegralWallet.setUpdateTime(dateTime);
		ddbIntegralWalletMapper.insert(ddbIntegralWallet);
		return ddbIntegralWallet;
	}
}
