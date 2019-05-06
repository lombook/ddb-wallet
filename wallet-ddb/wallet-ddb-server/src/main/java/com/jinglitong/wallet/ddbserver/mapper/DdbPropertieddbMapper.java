package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbPropertieddb;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface DdbPropertieddbMapper extends MyMapper<DdbPropertieddb> {
    List<DdbPropertieddb> selectByGroupName(String groupName);

}