package com.jinglitong.springshop.mapper;


import com.jinglitong.springshop.entity.DdbCustIntegralRecord;
import com.jinglitong.springshop.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DdbCustIntegralRecordMapper extends MyMapper<DdbCustIntegralRecord> {

	List<DdbCustIntegralRecord> getAssetsRecord(DdbCustIntegralRecord vo);
}