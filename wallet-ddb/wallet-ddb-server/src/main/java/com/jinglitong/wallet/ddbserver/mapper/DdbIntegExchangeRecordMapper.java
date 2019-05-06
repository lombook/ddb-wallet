package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import com.jinglitong.wallet.ddbapi.model.DdbIntegExchangeRecord;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegExchangeRecordVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

public interface DdbIntegExchangeRecordMapper extends MyMapper<DdbIntegExchangeRecord> {
	List<DdbIntegExchangeRecord> getRecord(DdbIntegExchangeRecordVo vo);
}