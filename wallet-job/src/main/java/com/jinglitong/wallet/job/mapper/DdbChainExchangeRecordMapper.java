package com.jinglitong.wallet.job.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRecord;
import com.jinglitong.wallet.job.util.MyMapper;

public interface DdbChainExchangeRecordMapper extends MyMapper<DdbChainExchangeRecord> {

	List<DdbChainExchangeRecord> getTransferDataByStateAndCount(int count);
	
	List<DdbChainExchangeRecord> getFailTransferDataByStateAndCount(int count);
	
	List<DdbChainExchangeRecord> getCheckDataByState(String secondtime);
	
	@Update(value = "update ddb_chain_exchange_record  set state= #{state},pay_hash=#{paymentHash},update_time=now() where zid = #{zid}")
	void updateStateHashByZid(@Param("state") int state, @Param("paymentHash") String paymentHash,@Param("zid") String zid);
	
	@Update(value = "update ddb_chain_exchange_record  set state= #{state},count= count + 1,update_time=now() where zid = #{zid}")
	void updateStateCountByZid(@Param("state") int state,  @Param("zid") String zid);
	
	@Update(value = "update ddb_chain_exchange_record  set state= #{state},update_time=now() where zid = #{zid}")
	void updateStateByZid(@Param("state") int state,  @Param("zid") String zid);

	@Update(value = "update ddb_chain_exchange_record  set count= count + 1,update_time=now() where zid = #{zid}")
	void updateCountByZid(String zid);

	
	
}