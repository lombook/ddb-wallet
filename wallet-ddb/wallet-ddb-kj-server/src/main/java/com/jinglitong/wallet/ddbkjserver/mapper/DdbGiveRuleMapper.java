package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbGiveRule;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import java.util.List;

public interface DdbGiveRuleMapper extends MyMapper<DdbGiveRule> {
   List<DdbGiveRule> selectBygroupName(String groupName);
}