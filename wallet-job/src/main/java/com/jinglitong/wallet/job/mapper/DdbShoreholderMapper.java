package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbShoreholder;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface DdbShoreholderMapper extends MyMapper<DdbShoreholder> {
    DdbShoreholder selectbyCustId(String userId);

    @Update("update ddb_shoreholder set customer_type = #{customerType} where customer_id = #{custId}")
    void updataTypeByCustIdAndType(@Param("customerType") Integer middleType, @Param("custId") String userId);
}