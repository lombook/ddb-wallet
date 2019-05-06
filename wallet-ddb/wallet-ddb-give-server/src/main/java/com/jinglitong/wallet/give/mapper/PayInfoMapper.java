package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.view.PayInfoVo;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.List;

public interface PayInfoMapper extends MyMapper<PayInfo> {
    List<PayInfo> selectBySelerId(PayInfoVo payInfoVo);
    List<PayInfo> selectByApp(PayInfoVo payInfoVo);
}