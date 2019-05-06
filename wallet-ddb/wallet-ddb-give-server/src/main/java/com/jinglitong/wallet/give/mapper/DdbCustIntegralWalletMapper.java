package com.jinglitong.wallet.give.mapper;


import java.util.Map;

import com.jinglitong.wallet.give.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralWallet;
import org.springframework.stereotype.Component;

@Component
public interface DdbCustIntegralWalletMapper extends MyMapper<DdbCustIntegralWallet> {
	
	int addBf(DdbCustIntegralWallet wallet);

	DdbCustIntegralWallet selectByCustId(String custId);
	
	Map<String, Object> getIntegralWallet(String custId);

	@Update(value = "update ddb_cust_integral_wallet  set baofen=baofen - #{amount}  where cust_id = #{custId}")
	void updateBalance(@Param("amount") Integer amount, @Param("custId") String custId);
	 
	
	
}