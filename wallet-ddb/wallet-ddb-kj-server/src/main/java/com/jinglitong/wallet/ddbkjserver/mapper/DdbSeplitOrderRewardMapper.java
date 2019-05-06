package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbSeplitOrderReward;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DdbSeplitOrderRewardMapper extends MyMapper<DdbSeplitOrderReward> {

    @Update("update ddb_seplit_order_reward set state = #{state} where zid = #{zid} ")
    void updateStateByzId(@Param("state") int i, @Param("zid") String zid);
}