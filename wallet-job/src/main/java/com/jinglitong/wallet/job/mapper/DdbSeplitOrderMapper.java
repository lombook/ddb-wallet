package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbSeplitOrder;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbSeplitOrderMapper extends MyMapper<DdbSeplitOrder> {
    @Update("update ddb_seplit_order set state = #{state} where zid = #{zid} ")
    void updateStateByzId(@Param("state") int i, @Param("zid") String zid);
}