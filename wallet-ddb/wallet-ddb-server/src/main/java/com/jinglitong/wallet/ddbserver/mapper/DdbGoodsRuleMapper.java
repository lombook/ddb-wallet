package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DdbGoodsRuleMapper extends MyMapper<DdbGoodsRule> {
    DdbGoodsRule selectByZid(String ruleId);

    List<DdbGoodsRule> selectAllGoodsRule();
}