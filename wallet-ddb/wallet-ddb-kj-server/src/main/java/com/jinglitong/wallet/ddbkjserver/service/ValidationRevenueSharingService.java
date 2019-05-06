package com.jinglitong.wallet.ddbkjserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.jinglitong.wallet.ddbapi.model.logic.FRuleInfo;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardRuleDetailMapper;

/**
 * 校验更新内存中的分红比列服务
 * 
 * @author pan
 *
 */
@Component
@EnableScheduling
public class ValidationRevenueSharingService {

	@Autowired
	DdbRewardRuleDetailMapper ddbRewardRuleDetailMapper;

	private Map<String, Map<String, String>> checkResultMap = new HashMap<>();

	//校验更新内存中的分红比列方法
	public void checkAndUpdateStart() {

		// 1.查询股东升级规则及分红规则明细表，获取分红比例详细信息
		List<FRuleInfo> fRuleInfos = ddbRewardRuleDetailMapper.selectDdbRewardRuleDetail();

		getSortBonusSystem(fRuleInfos);

		ConstantDict.INIT_BONUS_SYSTEM = checkResultMap;
	}

	// 2.根据获得级差分红比例，排列组合计算后得到详细的比例信息
	private void getSortBonusSystem(List<FRuleInfo> fRuleInfos) {
		
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
		// 获取极差分红信息，放入map中
		Map<String, String> levelFixedPercentMap = new HashMap<>();
		for (FRuleInfo fInfo : fRuleInfos) {
			levelFixedPercentMap.put("" + fInfo.getfLevel(),
					fInfo.getfRuleDetailID() + "_" + fInfo.getfRate().intValue());
		}
		// 循环遍历64排列组合情况
		for (String s : ruleList) {
			char[] chars = s.toCharArray();
			// 存放 任意一种组合结果， 对应的 6个等级的发放比例
			Map<String, String> map = new HashMap<>();
			int percentSum = 0; // 该股东之前的股东已分的的比例
			// 循环遍历每个组合，放入每个股东的应得分红比例
			for (int i = 1; i <= chars.length; i++) {
				int a = levelFixedPercentMap.get("" + i).indexOf("_");
				if (0 == Integer.parseInt(String.valueOf(chars[i - 1]))) {
					map.put(i + "", levelFixedPercentMap.get("" + i).substring(0, a) + "_" + 0);
				} else if (1 == Integer.parseInt(String.valueOf(chars[i - 1]))) {
					map.put(i + "", levelFixedPercentMap.get("" + i).substring(0, a) + "_"
							+ (Integer.parseInt(levelFixedPercentMap.get("" + i).substring(a + 1)) - percentSum));
					percentSum = Integer.parseInt(levelFixedPercentMap.get(i + "").substring(a + 1));
				}
			}
			checkResultMap.put(s, map);
		}
	}
}
