package com.jinglitong.wallet.ddbkjserver.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholderRule;
import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbShoreholderMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbShoreholderRuleMapper;
import com.jinglitong.wallet.ddbkjserver.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private DdbShoreholderMapper ddbShoreholderMapper;
	@Autowired
	private DdbShoreholderRuleMapper ddbShoreholderRuleMapper;
	
	/** 根用户 userID **/
	private String ROOT_USER_ID = "cf8c8ff81bd64887b2e2b9a71f71ec47";

	long a = 0;

	public void initUserTree() {
		/** 直接下级用户列表 */
		log.info(" ===== initUserTree begin ===== ");
		long startTime = System.currentTimeMillis();

		List<String> parentsList = new ArrayList<String>();
		// 根据根节点用户ID,查询其直接下级用户列表
		List<String> directChildrenList = customerMapper.getInvitorsByInviteUserId(ROOT_USER_ID);
		// 查询股东表
		DdbShoreholder selectbyCustId = ddbShoreholderMapper.selectbyCustId(ROOT_USER_ID);
		// 获得股东升级规则
        Map<String, DdbShoreholderRule> shoreholderRuleMap = getShoreholderRuleMap();
		// 组装根节点用户信息
		CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
		customerInfo.setUserid(ROOT_USER_ID);
		customerInfo.setParentsList(new ArrayList<String>());
		customerInfo.setDirectChildrenList(directChildrenList==null ? new ArrayList<String>() : directChildrenList);
		customerInfo.setTreeNodeLevel(parentsList.size() + 1);
		int holderLevel = 0;
		if(selectbyCustId.getLevelDefine()!=null
				&&!selectbyCustId.getLevelDefine().equals("")){
			holderLevel = shoreholderRuleMap.get(selectbyCustId.getLevelDefine()).getLevelSeqNo();
		}
		customerInfo.setSelfStockholderLevel(holderLevel);
		customerInfo.setSelfSumAmount(selectbyCustId.getSumAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getSumAmount()));
		customerInfo.setSelfYumiaoSumAmount(selectbyCustId.getSeedAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getSeedAmount()));
		customerInfo.setTeamSumAmount(selectbyCustId.getTeamSumAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getTeamSumAmount()));
		if (selectbyCustId.getTeamMemberLevelsCnt()!=null) {
			String teamMemberLevelsCnt = selectbyCustId.getTeamMemberLevelsCnt();
			Map teamMemberLevelsCntMap = JSON.parseObject(teamMemberLevelsCnt);
			customerInfo.setTeamMemberCntByStockholderLevel(teamMemberLevelsCntMap);
		}
		//设置自己购买的特殊商品
		if(selectbyCustId.getOwnSpecialNum()!=null){
			customerInfo.setOwnSpecialNum(selectbyCustId.getOwnSpecialNum());
		}
		//设置直接下级购买的特殊商品
		if(selectbyCustId.getDsubSpecialNum()!=null){
			customerInfo.setDsubSpecialNum(selectbyCustId.getDsubSpecialNum());
		}
		// 将根节点用户添加到用户树缓存Map中
		ConstantDict.CUSTOMER_TREE_DATA.put(ROOT_USER_ID, customerInfo);
		log.info("nodeLevel is : ======" + customerInfo.getTreeNodeLevel());

		// 组装根节点用户的下级用户信息，保存到树中
		if (directChildrenList != null && directChildrenList.size() > 0) {
			setLevelDatas(directChildrenList, parentsList, ROOT_USER_ID, shoreholderRuleMap);
		}
		
		long endTime = System.currentTimeMillis();
		log.info(" ===== initUserTree finished ===== all "+ ConstantDict.CUSTOMER_TREE_DATA.entrySet().size() + " user(s), cost time" + (endTime-startTime) + " ms.");
	}

	private Map<String, DdbShoreholderRule> getShoreholderRuleMap() {
		Map<String, DdbShoreholderRule> map = new HashMap<String, DdbShoreholderRule>();
		List<DdbShoreholderRule> ruleAll = ddbShoreholderRuleMapper.selectAllRule();
		for(DdbShoreholderRule ddbShoreholderRule : ruleAll) {
			
			Integer levelSeqNo = ddbShoreholderRule.getLevelSeqNo();
			map.put(levelSeqNo.toString(), ddbShoreholderRule);
			map.put(ddbShoreholderRule.getLevelDefine(), ddbShoreholderRule);
			
		}
		return map;
	}
	
	/**
	 * 递归保存：级差分红用户信息
	 * 
	 * @param directChildrenList
	 * @param parentsList
	 */
	public void setLevelDatas(List<String> directChildrenList, List<String> parentsList,
			String userId,  Map<String, DdbShoreholderRule> shoreholderRuleMap) {
		
		if (directChildrenList == null || directChildrenList.size()==0) {
			return;
		}

		// 将当前用户ID添加到其父用户List的最后，作为其直接下级用户的父用户List
		List<String> child_parentsList = new ArrayList<String>();
		child_parentsList.addAll(parentsList);
		child_parentsList.add(userId);
		
		for (String child_UserId : directChildrenList) {

			// 根据下级用户ID,查询其直接下级用户列表
			List<String> child_directChildrenList = customerMapper.getInvitorsByInviteUserId(child_UserId);
			// 查询股东表
			DdbShoreholder selectbyCustId = ddbShoreholderMapper.selectbyCustId(child_UserId);
			// 组装下级用户信息
			CustomerInfo4LevelDiff child_customerInfo = new CustomerInfo4LevelDiff();
			child_customerInfo.setUserid(child_UserId);
			child_customerInfo.setParentsList(child_parentsList);
			child_customerInfo.setTreeNodeLevel(child_parentsList.size() + 1);
			child_customerInfo.setDirectChildrenList(child_directChildrenList==null ? new ArrayList<String>() : child_directChildrenList);
			
			int holderLevel = 0;
			if(selectbyCustId.getLevelDefine()!=null
					&&!selectbyCustId.getLevelDefine().equals("")){
				holderLevel = shoreholderRuleMap.get(selectbyCustId.getLevelDefine()).getLevelSeqNo();
			}
			child_customerInfo.setSelfStockholderLevel(holderLevel);
			child_customerInfo.setSelfSumAmount(selectbyCustId.getSumAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getSumAmount()));
			child_customerInfo.setSelfYumiaoSumAmount(selectbyCustId.getSeedAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getSeedAmount()));
			child_customerInfo.setTeamSumAmount(selectbyCustId.getTeamSumAmount()==null?new BigDecimal(0):new BigDecimal(selectbyCustId.getTeamSumAmount()));
			if (selectbyCustId.getTeamMemberLevelsCnt()!=null&&!"".equals(selectbyCustId.getTeamMemberLevelsCnt())) {
				String teamMemberLevelsCnt = selectbyCustId.getTeamMemberLevelsCnt();
				log.info(selectbyCustId.getId()+" ====== "+teamMemberLevelsCnt);
				Map teamMemberLevelsCntMap = JSON.parseObject(teamMemberLevelsCnt);
				child_customerInfo.setTeamMemberCntByStockholderLevel(teamMemberLevelsCntMap);
			}
			//设置自己购买的特殊商品
			if(selectbyCustId.getOwnSpecialNum()!=null){
				child_customerInfo.setOwnSpecialNum(selectbyCustId.getOwnSpecialNum());
			}
			//设置直接下级购买的特殊商品
			if(selectbyCustId.getDsubSpecialNum()!=null){
				child_customerInfo.setDsubSpecialNum(selectbyCustId.getDsubSpecialNum());
			}
			// 将下级用户添加到用户树缓存Map中
			ConstantDict.CUSTOMER_TREE_DATA.put(child_UserId, child_customerInfo);
			log.info("nodeLevel is : ======" + child_customerInfo.getTreeNodeLevel() + 1);

			// 组装下级用户的再下级用户信息，保存到树中
			if (child_directChildrenList != null && child_directChildrenList.size() > 0) {
				setLevelDatas(child_directChildrenList, child_parentsList, child_UserId, shoreholderRuleMap);
			}
		}
	}
}
