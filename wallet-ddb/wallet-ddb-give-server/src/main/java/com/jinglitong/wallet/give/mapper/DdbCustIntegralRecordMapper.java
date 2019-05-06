package com.jinglitong.wallet.give.mapper;


import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.give.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DdbCustIntegralRecordMapper extends MyMapper<DdbCustIntegralRecord> {
	
	List<Map<String,Object>> getIntegralRecord(String custId);
	
	List<DdbCustIntegralRecord> getAssetsRecord(DdbCustIntegralRecord vo);
}