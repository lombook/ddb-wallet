package com.jinglitong.wallet.give.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveRecord;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.give.mapper.DdbActiveGiveRecordMapper;
import com.jinglitong.wallet.give.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.give.mapper.DdbMqMessageRecordMapper;

@Service
public class CalculateService {


	@Autowired
	private DdbActiveGiveRecordMapper ddbActiveGiveRecordMapper;
	
	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper; 
	
    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;
    
    @Transactional
    public void add(DdbActiveGiveRecord activeGiveRecord,List<DdbMqMessageRecord> msg,DdbConsumerHistory ddbConsumerInsert) {
    	ddbActiveGiveRecordMapper.insert(activeGiveRecord);
    	ddbMqMessageRecordMapper.insertList(msg);
    	ddbConsumerHistoryMapper.insert(ddbConsumerInsert);
    }
}
