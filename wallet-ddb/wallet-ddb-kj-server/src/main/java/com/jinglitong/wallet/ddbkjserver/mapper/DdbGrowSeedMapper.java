package com.jinglitong.wallet.ddbkjserver.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.jinglitong.wallet.ddbapi.model.DdbGrowSeed;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
public interface DdbGrowSeedMapper extends MyMapper<DdbGrowSeed> {
	 @Update("update ddb_grow_seed set state = #{state} where zid = #{flowId}")
	    void updateStateByZId(@Param("state") int state,@Param("flowId") String flowId);
	 /**
	  * 查询未处理的育苗订单
	  */
	 List<DdbGrowSeed> getNewGrowSeedsList(Map<String,Object> map);
}