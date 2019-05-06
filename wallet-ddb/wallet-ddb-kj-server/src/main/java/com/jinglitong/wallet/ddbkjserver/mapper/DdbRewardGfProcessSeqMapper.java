package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbRewardGfProcessSeq;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface DdbRewardGfProcessSeqMapper extends MyMapper<DdbRewardGfProcessSeq> {

	 @Update("update ddb_reward_gf_process_seq set state = #{state} , process_time = now() where order_zid = #{flowId}")
	 void updateByOrderZid(@Param("state") int state, @Param("flowId") String flowId);
}