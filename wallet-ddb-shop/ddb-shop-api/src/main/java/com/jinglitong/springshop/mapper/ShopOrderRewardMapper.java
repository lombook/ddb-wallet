package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.ShopOrderReward;
import com.jinglitong.springshop.utils.MyMapper;

public interface ShopOrderRewardMapper extends MyMapper<ShopOrderReward> {

    int getCountByFlowId(ShopOrderReward shopOrderReward);

    int saveShopOrderReward(ShopOrderReward shopOrderReward);

    int updateRewardState(ShopOrderReward shopOrderReward);
}
