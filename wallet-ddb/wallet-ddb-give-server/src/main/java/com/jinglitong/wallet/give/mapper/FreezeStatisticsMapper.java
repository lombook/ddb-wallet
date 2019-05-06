package com.jinglitong.wallet.give.mapper;

import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.api.model.FreezeUserInfo;
import com.jinglitong.wallet.api.model.view.FreezeDataVO;
import com.jinglitong.wallet.give.util.MyMapper;

public interface FreezeStatisticsMapper extends MyMapper<FreezeUserInfo> {
	List<FreezeUserInfo> getFreezeData(FreezeDataVO vo);

	Integer getTotalCount(String appId);

	List<Map<String, Object>> getTotalAmount(String appId);

}
