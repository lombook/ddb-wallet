package com.jinglitong.wallet.give.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbPropertieddb;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.List;

public interface DdbPropertieddbMapper extends MyMapper<DdbPropertieddb> {
    List<DdbPropertieddb> selectByGroupName(String groupName);

}