package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.give.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbOrderMapper extends MyMapper<DdbOrder> {
    @Update("update ddb_order set state = #{state} where flow_id = #{flowId}")
    void updateStateByFlowId(@Param("state") int state,@Param("flowId") String flowId);
}