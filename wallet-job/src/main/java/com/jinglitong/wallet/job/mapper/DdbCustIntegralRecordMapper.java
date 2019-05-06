package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DdbCustIntegralRecordMapper extends MyMapper<DdbCustIntegralRecord> {
	
	List<Map<String,Object>> getIntegralRecord(String custId);
	
	List<DdbCustIntegralRecord> getAssetsRecord(DdbCustIntegralRecord vo);
}