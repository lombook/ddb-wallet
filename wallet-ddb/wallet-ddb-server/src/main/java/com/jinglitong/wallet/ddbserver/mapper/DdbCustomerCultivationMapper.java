package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbCustomerCultivation;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DdbCustomerCultivationMapper extends MyMapper<DdbCustomerCultivation> {
    DdbCustomerCultivation selectByCustId(String userId);

    @Update("update ddb_customer_cultivation set cultivation_amount = cultivation_amount + #{amount},update_time = now()  where customer_id = #{custId}")
    void updateCultivationAmountByCustId(@Param("amount") Integer amount,@Param("custId") String userId);
}