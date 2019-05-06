package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.SellerCoinInfo;
import com.jinglitong.wallet.api.model.view.SellerCoinInfoVo;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.List;

public interface SellerCoinInfoMapper extends MyMapper<SellerCoinInfo> {
    List<SellerCoinInfo> selectBySellerId(SellerCoinInfoVo coinInfo);
}