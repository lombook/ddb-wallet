package com.jinglitong.wallet.checkserver.mapper;

import java.util.List;

import com.jinglitong.wallet.ddbapi.model.DdbRewardRule;
import com.jinglitong.wallet.checkserver.util.MyMapper;

public interface DdbRewardRuleMapper extends MyMapper<DdbRewardRule> {
	List<DdbRewardRule> selectByfruleGroup(String fruleGroup);
}