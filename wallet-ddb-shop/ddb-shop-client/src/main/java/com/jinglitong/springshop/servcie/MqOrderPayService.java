package com.jinglitong.springshop.servcie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.entity.MqMessageRecord;
import com.jinglitong.springshop.util.AliMQServiceUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class MqOrderPayService {
	
	@Autowired
    private MqMessageRecordService mqMessageRecordService;
	
	
	/**
     * 支付法币金额为零时，直接向paybank发送mq消息 设置支付完成
     * @param topic
     * @param groupId
     * @param tag
     * @param pid 支付事务zid
     */
    public void sendMqToPaybank(String topic,String groupId,String tag,String pid){
		try {
			int historyCount = mqMessageRecordService.getMqRecordCount(pid, tag);
			if(historyCount == 0) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("out_order_no", pid);
				log.info("put paybank data to Mq start");
				String hdResult =AliMQServiceUtil.createNewOrder(pid, JSON.toJSONString(map),topic, tag,groupId);
				log.info("put paybank data to Mq end");
				MqMessageRecord insert = buidBean(topic,pid,hdResult,tag,JSON.toJSONString(map),groupId);
				if(StringUtils.isEmpty(hdResult)) {
					insert.setStatus(false);
				}else {
					insert.setStatus(true);
				}
				mqMessageRecordService.insertRecord(insert);
				log.info("have put paybank data to Mq");
			}else {
				log.info("订单{}已处理paybank",pid);
			}
		} catch (Exception e) {
			log.info("订单{}处理reward异常",pid);
			e.printStackTrace();
		}
    }
    private MqMessageRecord buidBean (String topic,String uuid,String result ,String tag,String o,String groupId) {
    	MqMessageRecord msg = new MqMessageRecord();
    	msg.setFlowId(uuid);
    	msg.setGroupName(groupId);		
    	msg.setTopic(topic);
    	msg.setTag(tag);
    	msg.setMsgId(result);
    	msg.setDataBody(o);
    	msg.setSendType(3);
    	msg.setGroupType(1);
    	msg.setCreateTime(new Date());
    	return msg;
    }

}
