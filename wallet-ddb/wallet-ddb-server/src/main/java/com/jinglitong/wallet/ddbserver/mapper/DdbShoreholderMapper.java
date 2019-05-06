package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbShoreholderMapper extends MyMapper<DdbShoreholder> {
    DdbShoreholder selectbyCustId(String userId);
    @Update("update ddb_shoreholder set base_stone = #{baseStone} where cust_id = #{custId}")
    void updateBaseStoneByCustId(@Param("baseStone") int baseStone,@Param("custId") String custId);
}