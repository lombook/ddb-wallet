package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbGiveRule;
import com.jinglitong.wallet.ddbapi.model.DdbGrowRule;
import com.jinglitong.wallet.give.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DdbGrowRuleMapper extends MyMapper<DdbGrowRule> {
    List<DdbGrowRule> selectBygroupName(String groupName);
}