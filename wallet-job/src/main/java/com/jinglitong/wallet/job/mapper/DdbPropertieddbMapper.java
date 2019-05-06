package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbPropertieddb;
import com.jinglitong.wallet.job.util.MyMapper;

import java.util.List;

public interface DdbPropertieddbMapper extends MyMapper<DdbPropertieddb> {
    List<DdbPropertieddb> selectByGroupName(String groupName);

}