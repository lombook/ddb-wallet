package com.jinglitong.wallet.ddbkjserver.mapper;

import java.util.List;

import com.jinglitong.wallet.ddbapi.model.DdbRewardRule;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

public interface DdbRewardRuleMapper extends MyMapper<DdbRewardRule> {
	List<DdbRewardRule> selectByfruleGroup(String fruleGroup);
}