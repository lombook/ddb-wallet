package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.SellerCoinInfo;
import com.jinglitong.wallet.api.model.view.SellerCoinInfoVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;

public interface SellerCoinInfoMapper extends MyMapper<SellerCoinInfo> {
    List<SellerCoinInfo> selectBySellerId(SellerCoinInfoVo coinInfo);
}