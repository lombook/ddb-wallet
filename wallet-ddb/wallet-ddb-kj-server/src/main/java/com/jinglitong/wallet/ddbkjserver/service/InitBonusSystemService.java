package com.jinglitong.wallet.ddbkjserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.DdbSecExpandDic;
import com.jinglitong.wallet.ddbapi.model.logic.SecExpandDicInfo;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbSecExpandDicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbapi.model.logic.FRuleInfo;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardGfRuleDetailMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardRuleDetailMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 分红制度初始化服务
 * @author panzixin
 *
 */
@Service
@Transactional
@Slf4j
public class InitBonusSystemService {
	
	@Autowired
	DdbRewardRuleDetailMapper ddbRewardRuleDetailMapper;
	
	@Autowired
	DdbRewardGfRuleDetailMapper ddbRewardGfRuleDetailMapper;
	
	@Autowired
	DdbSecExpandDicMapper ddbSecExpandDicMapper;

	/**
	 * 分红制度初始化方法
	 */
	public void getDdbRewardRuleDetail(){
		//1.查询股东升级规则及分红规则明细表，获取分红比例详细信息
		List<FRuleInfo> fRuleInfos = ddbRewardRuleDetailMapper.selectDdbRewardRuleDetail();
		getSortBonusSystem(fRuleInfos);
		log.info("InitBonusSystemService : initInfo: "+ConstantDict.INIT_BONUS_SYSTEM);
		//初始化二级拓展分红规则，以ruleId作为key，list为value
		List<SecExpandDicInfo> ddbSecExpandDics = ddbSecExpandDicMapper.ddbSecExpandDicsDetail();
		if(ddbSecExpandDics!=null){
			List<SecExpandDicInfo> list = new ArrayList();
			for(SecExpandDicInfo dic: ddbSecExpandDics){
				if(!ConstantDict.INIT_SECOND_SYSTEM.containsKey(""+dic.getRuleId())){
					list = new ArrayList();
				}
				list.add(dic);
				ConstantDict.INIT_SECOND_SYSTEM.put(""+dic.getRuleId(),list);
			}
		}
		log.info("InitBonusSystemService : initInfo: "+ConstantDict.INIT_SECOND_SYSTEM);
	}
	//2.根据获得级差分红比例，排列组合计算后得到详细的比例信息
	public void getSortBonusSystem(List<FRuleInfo> fRuleInfos){
		//获取排列组合结果
		List<String> ruleList = new ArrayList<>();
		//从数据库中取出参加极差分红的股东层级数量
		List<String> levelDefineList = ddbRewardRuleDetailMapper.selectLevelNum(3);
		//将0~2^6的数字以二进制的形式，以字符串的形式保存到List<String>中。
		for(int i = 0;i < Math.pow(2, levelDefineList.size());i++) {
			StringBuilder sb = new StringBuilder();
			  for(int n = 5;n >= 0; n--) {
			      sb.append(i>>>n&1);
			  }
			ruleList.add(sb.toString());
		}
		
		//获取极差分红信息，放入map中
		Map<String,String> levelFixedPercentMap = new HashMap<>();
		for(FRuleInfo fInfo:fRuleInfos){
			levelFixedPercentMap.put(""+fInfo.getfLevel(), fInfo.getfRuleDetailID()+"_"+fInfo.getfRate().intValue());
			 log.info("InitBonusSystemService : bonusInfo: "+fInfo.getfLevel()+"::"+fInfo.getfRuleDetailID()+"_"+fInfo.getfRate().intValue());
		}
		//循环遍历所有的排列组合情况
		for(String s:ruleList){
			char[] chars =s.toCharArray();
			//存放 任意一种组合结果， 对应的 6个等级的发放比例
			Map<String,String> map = new HashMap<>();
			int percentSum=0;  //该股东之前的股东已分的的比例
			//循环遍历每个组合，放入每个股东的应得分红比例
			for(int i=1;i<=chars.length;i++){
				int a = levelFixedPercentMap.get(""+i).indexOf("_"); 
				if(0 ==Integer.parseInt(String.valueOf(chars[i-1]))){
					map.put(i+"",levelFixedPercentMap.get(""+i).substring(0, a)+"_"+0);
				}else if(1 ==Integer.parseInt(String.valueOf(chars[i-1]))){
					map.put(i+"",levelFixedPercentMap.get(""+i).substring(0, a)+"_"+(Integer.parseInt(levelFixedPercentMap.get(""+i).substring(a+1))- percentSum));
					percentSum = Integer.parseInt(levelFixedPercentMap.get(i+"").substring(a+1));
				}
			}
			ConstantDict.INIT_BONUS_SYSTEM.put(s,map);
		}
	}
	
	/**
	 * 分红制度初始化方法
	 */
	public void getDdbRewardGfRuleDetail(){
		//1.查询股东升级规则及分红规则明细表，获取分红比例详细信息
		List<FRuleInfo> fRuleInfos = ddbRewardGfRuleDetailMapper.selectDdbRewardRuleDetail();
		getSortBonusSystemGf(fRuleInfos);
		log.info("InitBonusSystemService : initInfo_GF: "+ConstantDict.INIT_BONUS_SYSTEM_GF);
	}
	//2.根据获得级差分红比例，排列组合计算后得到详细的比例信息
	public void getSortBonusSystemGf(List<FRuleInfo> fRuleInfos){
		//获取排列组合结果
		List<String> ruleList = new ArrayList<>();
		//从数据库中取出参加极差分红的股东层级数量
		List<String> levelDefineList = ddbRewardGfRuleDetailMapper.selectLevelNum(3);
		//将0~2^6的数字以二进制的形式，以字符串的形式保存到List<String>中。
		for(int i = 0;i < Math.pow(2, levelDefineList.size());i++) {
			StringBuilder sb = new StringBuilder();
			  for(int n = 5;n >= 0; n--) {
			      sb.append(i>>>n&1);
			  }
			ruleList.add(sb.toString());
		}
		
		//获取极差分红信息，放入map中
		Map<String,String> levelFixedPercentMap = new HashMap<>();
		for(FRuleInfo fInfo:fRuleInfos){
			levelFixedPercentMap.put(""+fInfo.getfLevel(), fInfo.getfRuleDetailID()+"_"+fInfo.getfRate().intValue());
			 log.info("InitBonusSystemService : bonusInfo_GF: "+fInfo.getfLevel()+"::"+fInfo.getfRuleDetailID()+"_"+fInfo.getfRate().intValue());
		}
		//循环遍历所有的排列组合情况
		for(String s:ruleList){
			char[] chars =s.toCharArray();
			//存放 任意一种组合结果， 对应的 6个等级的发放比例
			Map<String,String> map = new HashMap<>();
			int percentSum=0;  //该股东之前的股东已分的的比例
			//循环遍历每个组合，放入每个股东的应得分红比例
			for(int i=1;i<=chars.length;i++){
				int a = levelFixedPercentMap.get(""+i).indexOf("_"); 
				if(0 ==Integer.parseInt(String.valueOf(chars[i-1]))){
					map.put(i+"",levelFixedPercentMap.get(""+i).substring(0, a)+"_"+0);
				}else if(1 ==Integer.parseInt(String.valueOf(chars[i-1]))){
					map.put(i+"",levelFixedPercentMap.get(""+i).substring(0, a)+"_"+(Integer.parseInt(levelFixedPercentMap.get(""+i).substring(a+1))- percentSum));
					percentSum = Integer.parseInt(levelFixedPercentMap.get(i+"").substring(a+1));
				}
			}
			ConstantDict.INIT_BONUS_SYSTEM_GF.put(s,map);
		}
	}

}
