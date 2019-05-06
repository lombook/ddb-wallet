package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.job.util.MyMapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbOrderMapper extends MyMapper<DdbOrder> {
    @Update("update ddb_order set state = #{state} where flow_id = #{flowId}")
    void updateStateByFlowId(@Param("state") int state, @Param("flowId") String flowId);
    
    Map<String,String> selectCountByDate(@Param("yesTime") String yesTime,@Param("toTime") String toTime);
}