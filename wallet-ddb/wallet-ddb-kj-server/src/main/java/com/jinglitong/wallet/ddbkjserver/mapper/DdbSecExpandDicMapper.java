package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.logic.SecExpandDicInfo;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
import com.jinglitong.wallet.ddbapi.model.DdbSecExpandDic;

import java.util.List;

public interface DdbSecExpandDicMapper extends MyMapper<DdbSecExpandDic> {
    List<SecExpandDicInfo> ddbSecExpandDicsDetail();
}