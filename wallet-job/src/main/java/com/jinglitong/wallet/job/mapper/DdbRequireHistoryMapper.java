package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbRequireHistory;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface DdbRequireHistoryMapper extends MyMapper<DdbRequireHistory> {

    @Select("select * from ddb_require_history  where flow_id = #{flowId}")
    DdbRequireHistory selectByFlowId(String flowId);
}