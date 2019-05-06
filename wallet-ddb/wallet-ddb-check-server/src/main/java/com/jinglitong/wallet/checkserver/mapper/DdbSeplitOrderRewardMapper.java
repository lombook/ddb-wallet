package com.jinglitong.wallet.checkserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbSeplitOrderReward;
import com.jinglitong.wallet.checkserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DdbSeplitOrderRewardMapper extends MyMapper<DdbSeplitOrderReward> {

    @Update("update ddb_seplit_order_reward set state = #{state} where zid = #{zid} ")
    void updateStateByzId(@Param("state") int i, @Param("zid") String zid);
    
    Integer selectSumAmount(@Param("flow_id")String flow_id, @Param("cust_id")String cust_id, @Param("frule_id")String frule_id, @Param("state")int state);
    
}