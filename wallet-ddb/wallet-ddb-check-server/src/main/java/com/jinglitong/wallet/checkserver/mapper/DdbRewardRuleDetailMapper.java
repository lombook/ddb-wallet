package com.jinglitong.wallet.checkserver.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jinglitong.wallet.ddbapi.model.DdbRewardRuleDetail;
import com.jinglitong.wallet.ddbapi.model.logic.FRuleInfo;
import com.jinglitong.wallet.checkserver.util.MyMapper;

public interface DdbRewardRuleDetailMapper extends MyMapper<DdbRewardRuleDetail> {
	
	/**
	 * 查询每个等级股东的分红比例详情
	 * @return
	 */
	List<FRuleInfo> selectDdbRewardRuleDetail(); 
	
	
	Map<String, String> selectZidByRuleType(int type);
	
	/**
	 * 通过分红规则查询改规则下的股东等级定义数量
	 * @param ruleType
	 * @return
	 */
	List<String> selectLevelNum(int ruleType);
	
	int selectRateBylevelAndType(@Param("levelDefine")String levelDefine,@Param("ruleType")int ruleType);
}