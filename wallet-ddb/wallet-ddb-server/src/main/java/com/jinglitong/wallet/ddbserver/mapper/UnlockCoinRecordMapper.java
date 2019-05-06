package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import com.jinglitong.wallet.api.model.UnlockCoinRecord;


public interface UnlockCoinRecordMapper extends MyMapper<UnlockCoinRecord> {

    int insertAll(UnlockCoinRecord record);


    int updateById(UnlockCoinRecord record);
    
    List<UnlockCoinRecord> selectByStatus(@Param("status") int status);

}