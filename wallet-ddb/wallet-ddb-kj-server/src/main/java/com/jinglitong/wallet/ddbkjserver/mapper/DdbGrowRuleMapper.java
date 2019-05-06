package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbGiveRule;
import com.jinglitong.wallet.ddbapi.model.DdbGrowRule;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DdbGrowRuleMapper extends MyMapper<DdbGrowRule> {
    List<DdbGiveRule> selectBygroupName(String groupName);
}