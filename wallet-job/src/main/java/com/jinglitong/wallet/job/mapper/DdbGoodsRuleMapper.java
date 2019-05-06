package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbGoodsRuleMapper extends MyMapper<DdbGoodsRule> {
    DdbGoodsRule selectByZid(String ruleId);

    List<DdbGoodsRule> selectAllGoodsRule();
}