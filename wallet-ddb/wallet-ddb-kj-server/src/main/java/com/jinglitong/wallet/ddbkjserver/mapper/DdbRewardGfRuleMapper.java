package com.jinglitong.wallet.ddbkjserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbRewardGfRule;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import java.util.List;

public interface DdbRewardGfRuleMapper extends MyMapper<DdbRewardGfRule> {
	List<DdbRewardGfRule> selectByfruleGroup(String fruleGroup);
}