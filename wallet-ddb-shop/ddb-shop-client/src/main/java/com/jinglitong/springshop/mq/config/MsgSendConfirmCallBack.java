/*package com.jinglitong.springshop.mq.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.MqMessageRecord;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.servcie.MqMessageRecordService;
import com.jinglitong.springshop.utils.ApplicationContextHelper;
import com.jinglitong.springshop.utils.UuidUtil;
 
public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
	
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        	MqMessageRecordService service = (MqMessageRecordService)ApplicationContextHelper.getBean("mqMessageRecordService");
        	MqMessageRecord record = new MqMessageRecord();
        	String flowId =correlationData.getId();
        	record.setFlowId(flowId);
            if (ack) {
            	record.setStatus(1);
                System.out.println("消息确认成功cause"+cause+",correlationData="+correlationData.getId());
            } else {
            	record.setStatus(0);
                //处理丢失的消息
                System.out.println("消息确认失败:"+correlationData.getId()+"#cause"+cause);
            }
            service.update(record);
        }
    }
*/