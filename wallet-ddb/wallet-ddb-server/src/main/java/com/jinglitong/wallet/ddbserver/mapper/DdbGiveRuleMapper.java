package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbGiveRule;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;

public interface DdbGiveRuleMapper extends MyMapper<DdbGiveRule> {
   List<DdbGiveRule> selectBygroupName(String groupName);
}