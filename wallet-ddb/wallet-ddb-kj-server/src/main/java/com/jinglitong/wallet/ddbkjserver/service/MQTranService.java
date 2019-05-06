package com.jinglitong.wallet.ddbkjserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbRewardProcessSeq;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardProcessSeqMapper;

@Service
public class MQTranService {

	@Autowired
	private DdbRewardProcessSeqMapper ddbRewardProcessSeqMapper;
	
	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper; 
	
    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;
    
    @Transactional
	public void  add(DdbRewardProcessSeq rewardSeq,DdbConsumerHistory ddbConsumerInsert) {
    	ddbRewardProcessSeqMapper.insert(rewardSeq);
    	//ddbMqMessageRecordMapper.insert(msg);
    	ddbConsumerHistoryMapper.insert(ddbConsumerInsert);
	}
}
