package com.jinglitong.wallet.ddbkjserver.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbShoreholderMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户层级结构校验服务
 * @author panzixin
 *
 */
@Transactional
@Service
@Slf4j
public class ShareholdersHierarchyValidationService {

	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	DdbShoreholderMapper ddbShoreholderMapper;
	
	public void startCheck(){
		//查询用户信息表获取所有的用户id信息
		List<String> userIdList = customerMapper.getAllUserId();
		if(userIdList != null && userIdList.size() > 0){
			//遍历每个用户，通过用户id获取该用户的信息及股东信息
			for(String userid : userIdList){
				//通过用户id获取内存中的该用户的层级结构
				CustomerInfo4LevelDiff customerInfo = ConstantDict.CUSTOMER_TREE_DATA.get(userid);
				//通过用户id获取数据库中该用户的股东身份信息
				DdbShoreholder ddbShoreholder = ddbShoreholderMapper.selectbyCustId(userid);
				if(ddbShoreholder != null && customerInfo != null){
					/**需要校验的数据：
					1.内存中自己个人消费总额是否等于数据库中自己个人消费总额（不包含自己育苗订单）
					2.内存中自己育苗消费总额是否等于数据库中自己育苗消费总额
					3.内存中自己的团队消费总额是否等于数据库中自己的团队消费总额（包含自己育苗订单+直接下级每个人的个人+直接下级每个人的团队）**/
					//数据一致则查询下一个用户，不一致则以数据库中的数据为准，更新内存中的该用户数据
					//1.内存中自己个人消费总额是否等于数据库中自己个人消费总额（不包含自己育苗订单）
					if(customerInfo.getSelfSumAmount().compareTo(new BigDecimal(ddbShoreholder.getSumAmount())) != 0){
						customerInfo.setSelfSumAmount(new BigDecimal(ddbShoreholder.getSumAmount()));
					}
					//2.内存中自己育苗消费总额是否等于数据库中自己育苗消费总额
					if(customerInfo.getSelfYumiaoSumAmount().compareTo(new BigDecimal(ddbShoreholder.getSeedAmount())) != 0){
						customerInfo.setSelfYumiaoSumAmount(new BigDecimal(ddbShoreholder.getSeedAmount()));
					}
					//3.内存中自己的团队消费总额是否等于数据库中自己的团队消费总额（包含自己育苗订单+直接下级每个人的个人+直接下级每个人的团队）
					//从数据库中获取该用户的直接下级用户列表
					List<String> ddbDirectChildrenList = customerMapper.getInvitorsByInviteUserId(userid);
					BigDecimal ddbTotalSumAmount = new BigDecimal(0);
					
					if(ddbDirectChildrenList != null && ddbDirectChildrenList.size() > 0){
					
						for(String childernid : ddbDirectChildrenList){
							
							/*判断内存中该用户的childrenList中是否存在该用户下级用户id,若不存在，添加进内存childrenList中，
						      并将该children的团队购买总额及自身购买额，到该用户的teamAmount中*/
							CustomerInfo4LevelDiff customerInfo4LevelDiff = ConstantDict.CUSTOMER_TREE_DATA.get(childernid);
							if(customerInfo4LevelDiff == null){
								//将该下级用户加入内存树中的childrenLilst
								ConstantDict.CUSTOMER_TREE_DATA.get(userid).getDirectChildrenList().add(childernid);
								//从数据库中获取该下级用户的个人消费及团队消费总额,将总额加入内存中的TeamSumAmount;
								DdbShoreholder ddbShoreholder2 = ddbShoreholderMapper.selectbyCustId(childernid);
								BigDecimal teamAmountAndSelfSeedAmount = new BigDecimal(ddbShoreholder2.getSumAmount()).add(new BigDecimal(ddbShoreholder2.getTeamSumAmount()));
								ConstantDict.CUSTOMER_TREE_DATA.get(userid).getTeamSumAmount().add(teamAmountAndSelfSeedAmount);
								//股级人数加1
								Map<String, Integer> numMap = ConstantDict.CUSTOMER_TREE_DATA.get(userid).getTeamMemberCntByStockholderLevel();
								if(numMap != null){
									numMap.put(ddbShoreholder2.getLevelDefine(), numMap.get(ddbShoreholder2.getLevelDefine())+1);
								}
							}else {
								//从数据库中获取该下级用户的个人消费及团队消费总额
								DdbShoreholder ddbShoreholder2 = ddbShoreholderMapper.selectbyCustId(childernid);
								//判断内存中的数据是否同数据库中的相同，不同则更新  自己消费、自己买苗消费、自己团队消费；
								if(customerInfo4LevelDiff.getSelfSumAmount().compareTo(new BigDecimal(ddbShoreholder2.getSumAmount())) != 0){
									customerInfo4LevelDiff.setSelfSumAmount(new BigDecimal(ddbShoreholder2.getSumAmount()));
								}
								if(customerInfo4LevelDiff.getSelfYumiaoSumAmount().compareTo(new BigDecimal(ddbShoreholder2.getSeedAmount())) != 0) {
									customerInfo4LevelDiff.setSelfYumiaoSumAmount(new BigDecimal(ddbShoreholder2.getSeedAmount()));
								}
								if(customerInfo4LevelDiff.getTeamSumAmount().compareTo(new BigDecimal(ddbShoreholder2.getTeamSumAmount())) != 0) {
									customerInfo4LevelDiff.setTeamSumAmount(new BigDecimal(ddbShoreholder2.getTeamSumAmount()));
								}
								BigDecimal teamAmountAndSelfSeedAmount = new BigDecimal(ddbShoreholder2.getSumAmount()).add(new BigDecimal(ddbShoreholder2.getTeamSumAmount()));
								ddbTotalSumAmount.add(teamAmountAndSelfSeedAmount);
							}
						}
					}
					ddbTotalSumAmount = ddbTotalSumAmount.add(new BigDecimal(ddbShoreholder.getSeedAmount()));
					if(customerInfo.getTeamSumAmount().compareTo(ddbTotalSumAmount) != 0){
						customerInfo.setTeamSumAmount(ddbTotalSumAmount);
					}
				}
			}
		}
	}
	
}
