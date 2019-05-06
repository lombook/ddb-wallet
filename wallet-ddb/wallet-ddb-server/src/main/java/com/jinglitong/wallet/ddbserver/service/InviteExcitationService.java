package com.jinglitong.wallet.ddbserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.api.model.InviteSystem;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.DdbOrderAnalysisDetail;
import com.jinglitong.wallet.ddbapi.model.DdbPropertieddb;
import com.jinglitong.wallet.ddbserver.common.CustLevelTypeEnum;
import com.jinglitong.wallet.ddbserver.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbOrderAnalysisDetailMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbPropertieddbMapper;
import com.jinglitong.wallet.ddbserver.mapper.InviteSystemMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: InviteExcitationService.java
 * 作        者: yxl 2018年9月27日
 * 创建时间: 2018年9月27日
 * 功能说明:邀请激励service
 */
@Service
@Transactional(rollbackFor = { Exception.class },propagation=Propagation.REQUIRED)
@Slf4j
public class InviteExcitationService {

	/*@Autowired
	private DdbOrderAnalysisDetailMapper ddbOrderAnalysisDetailMapper;
	
	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper; 
	
	@Autowired
	private InviteSystemMapper inviteSystemMapper;
	
	
	@Autowired
	private  DdbCustIntegralWalletService ddbCustIntegralWalletService;
	
	@Autowired
	private DdbIntegralAccountService ddbIntegralAccountService;
	
	@Autowired
	private InviteSystemService inviteSystemService;
	
	@Autowired
	private DdbPropertieddbMapper ddbPropertieddbMapper;
	
	*//**
	 * 邀请总账类型
	 *//*
	@Value("${in.acc.type}")
	private int inType;
	*//**
	 * 差级总账类型
	 *//*
	@Value("${range.acc.type}")
	private int rangeType;
	
	*//**
	 * 配置表级差key
	 *//*
	@Value("${range.key}")
	private String rangeKey;
	
	*//**
	 * 
	 * 功能说明:邀请激励
	 * @param ruleId 规则id
	 * @param flowId 订单id
	 * @param productNum 产品数量
	 * @param userId 用户id
	 *//*
	public void excitation(DdbOrder order) {
		log.info("邀请激励begin");
		DdbGoodsRule rule = new DdbGoodsRule();
		rule.setZid(order.getRuleId());
		//查詢規則表
		rule = ddbGoodsRuleMapper.selectOne(rule);
		List<DdbOrderAnalysisDetail> arrList = new ArrayList<>();
		DdbOrderAnalysisDetail oneDetail = new DdbOrderAnalysisDetail(); 
		oneDetail.setFlowId(order.getFlowId());
		oneDetail.setRuleId(order.getRuleId());
		//商品数量
		int productNum = Integer.valueOf(order.getProductNum());
		//金額
		oneDetail.setPrice(rule.getProductPrice() * productNum);
		//赠送宝分
		oneDetail.setGiftBaofen(rule.getGiftBaofen() * productNum);
		oneDetail.setCustId(order.getUserId());
		
		InviteSystem invite = new InviteSystem();
		invite.setCustId(order.getUserId());
		//查询用户的邀请人信息
		invite = inviteSystemMapper.selectOne(invite);
		if(null != invite) {
			//一级邀请人是否为空
			if(!StringUtils.isEmpty(invite.getLevelOneCustId())) {
				log.info("一级邀请人begin");
				oneDetail.setInviteId(invite.getLevelOneCustId());
				oneDetail.setInviteBaofen(rule.getLevelOneBaofen() * productNum);
				//邀请总账
				DdbIntegralAccount inviteAccount = ddbIntegralAccountService.selectOne(inType);
				//给一级邀请用户增宝分转账、总账减少宝分
				DdbCustIntegralWallet oneWallet = new DdbCustIntegralWallet(); 
				oneWallet.setBaofen(oneDetail.getInviteBaofen());
				oneWallet.setCustId(invite.getLevelOneCustId());
				oneDetail.setCreateTime(DateUtils.getDateTime());
				oneDetail.setLevel(1);
				arrList.add(oneDetail);
				if(oneWallet.getBaofen() != 0) {
					ddbCustIntegralWalletService.addBf(oneWallet,order.getFlowId(),1,inviteAccount.getZid());
				}
				//二级邀请人是否为空
				if(!StringUtils.isEmpty(invite.getLevelTwoCustId())) {
					log.info("二级邀请人begin");
					DdbOrderAnalysisDetail twoDetail = new DdbOrderAnalysisDetail(); 
					twoDetail = bulidDetail(oneDetail);
					twoDetail.setInviteId(invite.getLevelTwoCustId());
					twoDetail.setInviteBaofen(rule.getLevelTwoBaofen() * productNum);
					//给二级邀请用户增宝分转账、总账减少宝分
					DdbCustIntegralWallet twoWallet = new DdbCustIntegralWallet(); 
					twoWallet.setBaofen(twoDetail.getInviteBaofen());
					twoWallet.setCustId(invite.getLevelTwoCustId());
					if(twoWallet.getBaofen() != 0) {
						ddbCustIntegralWalletService.addBf(twoWallet,order.getFlowId(),1,inviteAccount.getZid());
					}
					twoDetail.setLevel(2);
					twoDetail.setCreateTime(DateUtils.getDateTime());
					arrList.add(twoDetail);
					log.info("二级邀请人end");
				}
				//股东分红
				if(rule.getShareBaofen()) {
					List<String> list = inviteSystemService.getInviteUser(order.getUserId());
					if(list.size() > 0) {
						log.info("股东分红begin");
						//配置表查询级差值
						DdbPropertieddb prp = new DdbPropertieddb();
						prp.setGroupKey(rangeKey);
						prp = ddbPropertieddbMapper.selectOne(prp);
						//差级总账
						DdbIntegralAccount rangeAccount = ddbIntegralAccountService.selectOne(rangeType);
						String [] oneOb = list.get(0).split(",");
						String oneCustId = oneOb[0];
						int oneType = Integer.valueOf(oneOb[1]);
						int range = Integer.valueOf(prp.getGroupValue());
						//3:创业股东
						if(CustLevelTypeEnum.STARTING.getType()== oneType) {
							range(range, 3, oneCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
							// 2:合伙股东
						}else if(CustLevelTypeEnum.PARTNERSHIP.getType()== oneType) {
							range(range, 2, oneCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
							//任意股东
							if(list.size() > 1) {
								String twoCustId = list.get(1).split(",")[0];
								range(range, 1, twoCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
							}
							//1:新创股东
						}else if(CustLevelTypeEnum.NEWCREATE.getType()== oneType) {
							range(range, 1, oneCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
							if(list.size() > 1) {
								String [] twoOb = list.get(1).split(",");
								String twoCustId = twoOb[0];
								int twoType = Integer.valueOf(twoOb[1]);
								//2:合伙股东  3:创业股东
								if(CustLevelTypeEnum.PARTNERSHIP.getType()== twoType || CustLevelTypeEnum.STARTING.getType()== twoType) {
									range(range, 2, twoCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
									//1:新创股东
								}else if (CustLevelTypeEnum.NEWCREATE.getType()== twoType) {
									range(range, 1, twoCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
									//任意股东
									if(list.size() > 2) {
										String threeCustId = list.get(2).split(",")[0];
										range(range, 1, threeCustId, oneDetail.getGiftBaofen(), order.getFlowId(), rangeAccount.getZid());
									}
								}
							}
						}
						log.info("股东分红end");
					}
					
				}
				log.info("一级邀请人end");
			}
		}else {
			oneDetail.setCreateTime(DateUtils.getDateTime());
			arrList.add(oneDetail);
		}
		//订单详情表入库
		ddbOrderAnalysisDetailMapper.insertList(arrList);
		log.info("邀请激励end");
	}
	
	private void range(int perc ,int multi,String custId,int giftBaofen,String flowId,String accountId) {
		DdbCustIntegralWallet range = new DdbCustIntegralWallet(); 
		range.setBaofen((perc * multi * giftBaofen)/100);
		range.setCustId(custId);
		if(range.getBaofen() != 0) {
			ddbCustIntegralWalletService.addBf(range,flowId,3,accountId);
		}
	}
	public DdbOrderAnalysisDetail bulidDetail(DdbOrderAnalysisDetail detail) {
		DdbOrderAnalysisDetail build = new DdbOrderAnalysisDetail();
		build.setFlowId(detail.getFlowId());
		build.setRuleId(detail.getRuleId());
		build.setPrice(detail.getPrice());//金額
		build.setGiftBaofen(detail.getGiftBaofen());//赠送宝分
		build.setCustId(detail.getCustId());
		return build;
	}*/
	
}
