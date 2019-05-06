package com.jinglitong.springshop.servcie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.springshop.entity.MqMessageRecord;
import com.jinglitong.springshop.mapper.MqMessageRecordMapper;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class MqMessageRecordService {

	@Autowired
	private MqMessageRecordMapper mqMessageRecordMapper;
	
	
	@Transactional
	public void  insertRecord(MqMessageRecord record) {
		 mqMessageRecordMapper.insert(record);
	}
	public int update (MqMessageRecord record) {
    	Weekend<MqMessageRecord>weekend = Weekend.of(MqMessageRecord.class);
    	WeekendCriteria<MqMessageRecord,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(MqMessageRecord::getFlowId, record.getFlowId());
		return mqMessageRecordMapper.updateByExampleSelective(record, weekend);
	}
	
	public List<MqMessageRecord> selectByRecord(MqMessageRecord record){
		return mqMessageRecordMapper.select(record);
		
	}
	
	public int getMqRecordCount(String flowId,String tag) {
		MqMessageRecord record = new MqMessageRecord();
		record.setFlowId(flowId);
		record.setTag(tag);
		return mqMessageRecordMapper.selectCount(record);
		
	}
	
	
}
