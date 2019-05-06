package com.jinglitong.wallet.ddbkjserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbCustExchangeRecord;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
@Component
public interface DdbCustExchangeRecordMapper extends MyMapper<DdbCustExchangeRecord> {

    @Update(value = "update ddb_asset_record  set real_state =#{state}  where cust_id = #{custId}")
    void updateRealStateByCustId(@Param("state") int state,@Param("custId") String custId);
}