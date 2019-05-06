package com.jinglitong.springshop.mq;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.MqMessageRecord;
import com.jinglitong.springshop.mapper.MqMessageRecordMapper;
import com.jinglitong.springshop.util.AliMQServiceUtil;

@Service
public class MqProductService {

    @Resource
	private MqMessageRecordMapper mqMessageRecordMapper;
    
	public void start() {
		MqMessageRecord record = new MqMessageRecord();
		record.setStatus(false);
		List<MqMessageRecord> list = mqMessageRecordMapper.select(record);
		for (MqMessageRecord md : list) {
			try {
				String hdResult =AliMQServiceUtil.createNewOrder(md.getFlowId(), md.getDataBody(),md.getTopic(), md.getTag(),md.getGroupName());
				if(!StringUtils.isEmpty(hdResult)) {
					MqMessageRecord update = new MqMessageRecord();
					update.setId(md.getId());
					update.setStatus(true);
					mqMessageRecordMapper.updateByPrimaryKeySelective(update);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}