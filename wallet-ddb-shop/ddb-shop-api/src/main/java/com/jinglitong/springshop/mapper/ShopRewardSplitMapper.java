package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.ShopRewardSplit;
import com.jinglitong.springshop.utils.MyMapper;

import java.util.List;

public interface ShopRewardSplitMapper extends MyMapper<ShopRewardSplit> {

    int saveShopRewardSplit(ShopRewardSplit shopRewardSplit);

    List<ShopRewardSplit> getShopRewardSplitList(ShopRewardSplit shopRewardSplit);

    int updateShopRewardSplit(ShopRewardSplit shopRewardSplit);
}
