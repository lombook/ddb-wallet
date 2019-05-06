package com.jinglitong.wallet.ddbserver.mapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface DdbCustIntegralRecordMapper extends MyMapper<DdbCustIntegralRecord> {
	
	List<Map<String,Object>> getIntegralRecord(String custId);
	
	List<DdbCustIntegralRecord> getAssetsRecord(DdbCustIntegralRecord vo);

}