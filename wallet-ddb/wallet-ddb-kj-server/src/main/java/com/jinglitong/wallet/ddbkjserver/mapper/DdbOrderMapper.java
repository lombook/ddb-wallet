package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbOrderMapper extends MyMapper<DdbOrder> {
    @Update("update ddb_order set kj_state = #{state} where flow_id = #{flowId}")
    void updateStateByFlowId(@Param("state") int state,@Param("flowId") String flowId);
    
    /**
     * 从订单表中，查找所有未处理的订单
     * @return
     */
    List<DdbOrder> selectAllUntreatedOrder();
    
}