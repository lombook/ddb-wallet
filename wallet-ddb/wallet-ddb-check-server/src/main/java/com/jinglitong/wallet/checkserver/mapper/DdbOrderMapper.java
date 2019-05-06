package com.jinglitong.wallet.checkserver.mapper;

import com.jinglitong.wallet.checkserver.util.MyMapper;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbOrderMapper extends MyMapper<DdbOrder> {
    @Update("update ddb_order set state = #{state} where flow_id = #{flowId}")
    void updateStateByFlowId(@Param("state") int state,@Param("flowId") String flowId);
    
    /**
     * 从订单表中，查找所有未处理的订单
     * @return
     */
    List<DdbOrder> selectAllUntreatedOrder();
    
}