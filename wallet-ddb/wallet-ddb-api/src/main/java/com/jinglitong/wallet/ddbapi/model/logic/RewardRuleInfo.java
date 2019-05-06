package com.jinglitong.wallet.ddbapi.model.logic;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

/**
 * 分红规则信息VO
 */
@Data
public class RewardRuleInfo {
	
	/** 
	 * 规则类型
	 */
	private String ruleType;
	/**
	 * 分红基数比例
	 */
	private BigDecimal rewardRate;
	/** 
	 * 积分分红参数Map
	 * 数据格式:Map<积分名称(real_name), 总账积分ZID(inte_zid)+":"+分红比例(rate)>
	 */
	private Map<String, String> rewardArgsMap;
	/** 
	 * 拓展分红比例
	 * （规则类型ruleType=1or2时有值,3or4时为空）
	 */
	private BigDecimal rewardPercent;
	/** 
	 * 分红比例明细Map
	 * 数据格式:Map<股东等级名称(shoreholder_level_define), 分红规则明细ZID+":"+分红比例(reward_percent)>
	 * （规则类型ruleType=4时有值,1or2or3时为空）
	 */
	private Map<String, String> ruleDetailMap;
	
}