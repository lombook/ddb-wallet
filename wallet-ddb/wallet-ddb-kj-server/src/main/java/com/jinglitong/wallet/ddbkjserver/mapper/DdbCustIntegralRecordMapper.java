package com.jinglitong.wallet.ddbkjserver.mapper;


import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

public interface DdbCustIntegralRecordMapper extends MyMapper<DdbCustIntegralRecord> {
	
	List<Map<String,Object>> getIntegralRecord(String custId);
	
	List<DdbCustIntegralRecord> getAssetsRecord(DdbCustIntegralRecord vo);
}