package com.jinglitong.springshop.mapper;


import com.jinglitong.springshop.entity.DdbIntegralWallet;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbIntegralWalletMapper extends MyMapper<DdbIntegralWallet> {

    /**
     * 通过用户id和name查找用户钱包
     * @param integName
     * @param custId
     * @return
     */
    DdbIntegralWallet selectByRealNameAdnCustId(@Param("integName") String integName, @Param("custId") String custId);

    @Update("update ddb_integral_wallet set amount = amount + #{amount} where cust_id = #{custId} and inte_name = #{integName} and amount + #{amount} > 0")
    int addInteger(@Param("amount") long amount,@Param("integName") String integName,@Param("custId") String custId);

    int updateByPay(DdbIntegralWallet wallet);
}