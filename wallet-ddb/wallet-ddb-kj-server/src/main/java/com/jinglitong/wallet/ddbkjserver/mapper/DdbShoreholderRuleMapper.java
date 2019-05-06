package com.jinglitong.wallet.ddbkjserver.mapper;

import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.DdbShoreholderRule;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;
public interface DdbShoreholderRuleMapper extends MyMapper<DdbShoreholderRule> {

	List<DdbShoreholderRule> selectAllRule();
}