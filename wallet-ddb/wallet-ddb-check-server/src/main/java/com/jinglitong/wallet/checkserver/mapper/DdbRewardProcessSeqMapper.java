package com.jinglitong.wallet.checkserver.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jinglitong.wallet.checkserver.util.MyMapper;
import com.jinglitong.wallet.ddbapi.model.DdbRewardProcessSeq;

public interface DdbRewardProcessSeqMapper extends MyMapper<DdbRewardProcessSeq> {

	 @Update("update ddb_reward_process_seq set state = #{state} , process_time = now() where order_zid = #{flowId}")
	 void updateByOrderZid(@Param("state") int state,@Param("flowId") String flowId);
}