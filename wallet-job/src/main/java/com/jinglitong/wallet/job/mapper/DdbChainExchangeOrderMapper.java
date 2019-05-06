package com.jinglitong.wallet.job.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeOrder;
import com.jinglitong.wallet.job.util.MyMapper;

public interface DdbChainExchangeOrderMapper extends MyMapper<DdbChainExchangeOrder> {

	
	@Update(value = "update ddb_chain_exchange_order  set state= #{state},update_time=now() where zid = #{zid}")
	void updateStateByZid(@Param("state") int state,  @Param("zid") String zid);
}