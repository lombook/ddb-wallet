package com.jinglitong.wallet.give.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.give.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DdbGoodsRuleMapper extends MyMapper<DdbGoodsRule> {
    DdbGoodsRule selectByZid(String ruleId);

    List<DdbGoodsRule> selectAllGoodsRuleByZid(@Param("zid") String zid);
}