package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.view.PayInfoVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;

public interface PayInfoMapper extends MyMapper<PayInfo> {
    List<PayInfo> selectBySelerId(PayInfoVo payInfoVo);
    List<PayInfo> selectByApp(PayInfoVo payInfoVo);
}