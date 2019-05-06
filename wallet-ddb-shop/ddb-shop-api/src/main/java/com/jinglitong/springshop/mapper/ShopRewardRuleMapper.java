package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.ShopRewardRule;
import com.jinglitong.springshop.utils.MyMapper;

public interface ShopRewardRuleMapper extends MyMapper<ShopRewardRule> {

    ShopRewardRule getShopRewardRule(ShopRewardRule shopRewardRule);


}
