package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbSeplitOrderRewardGf;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DdbSeplitOrderRewardGfMapper extends MyMapper<DdbSeplitOrderRewardGf> {

    @Update("update ddb_seplit_order_reward_gf set state = #{state} where zid = #{zid} ")
    void updateStateByzId(@Param("state") int i, @Param("zid") String zid);
}