/*package com.jinglitong.springshop.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.mq.config.MsgSendConfirmCallBack;
import com.jinglitong.springshop.mq.config.MsgSendReturnCallback;
import com.jinglitong.springshop.utils.UuidUtil;

import java.util.UUID;
 
*//**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: ProductService.java
 * 作        者: yxl 2019年1月23日
 * 创建时间: 2019年1月23日
 * 功能说明:rabbitMq
 *//*
@Service
public class ProductRabbitMqService {
    private Logger logger= LoggerFactory.getLogger(ProductRabbitMqService.class);
    
    @Autowired
    RabbitAdmin rabbitAdmin;
 
    @Bean
    @Scope("prototype")
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.getRabbitTemplate().setConfirmCallback(new MsgSendConfirmCallBack());
        rabbitAdmin.getRabbitTemplate().setReturnCallback(new MsgSendReturnCallback());
        return rabbitAdmin;
    }
 
 
    public void send() throws Exception {
        String sendStr;
        for(int i=0;i<100000;i++){
            sendStr="第["+i+"]个 hello  --" + new Date();
            logger.info("HelloSender: " + sendStr);
            sendMessage(RabbitConstant.ORDER_QUEUE_NAME,sendStr);
            count.getAndIncrement();
        }
        logger.info("all count ={}",count.get());
    }
 
    *//**
     * 动态声明exchange和queue它们的绑定关系  rabbitAdmin
     * @param exchangeName
     * @param queueName
     *//*
    protected void declareBinding(String exchangeName, String queueName) {
        if (rabbitAdmin.getQueueProperties(queueName) == null) {
              queue 队列声明
            durable=true,交换机持久化,rabbitmq服务重启交换机依然存在,保证不丢失; durable=false,相反
            auto-delete=true:无消费者时，队列自动删除; auto-delete=false：无消费者时，队列不会自动删除
            排他性，exclusive=true:首次申明的connection连接下可见; exclusive=false：所有connection连接下
            Queue queue = new Queue(queueName, true, false, false, null);
            rabbitAdmin.declareQueue(queue);
            TopicExchange directExchange = new TopicExchange(exchangeName);
            rabbitAdmin.declareExchange(directExchange);//声明exchange
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);    //将queue绑定到exchange
            rabbitAdmin.declareBinding(binding);      //声明绑定关系
            rabbitAdmin.getRabbitTemplate().setMandatory(true);
            rabbitAdmin.getRabbitTemplate().setConfirmCallback(new MsgSendConfirmCallBack());//消息确认
            rabbitAdmin.getRabbitTemplate().setReturnCallback(new MsgSendReturnCallback());//确认后回调
        } else {
            rabbitAdmin.getRabbitTemplate().setQueue(queueName);
            rabbitAdmin.getRabbitTemplate().setExchange(queueName);
            rabbitAdmin.getRabbitTemplate().setRoutingKey(queueName);
        }
    }
 
    *//**
     * 发送消息
     * @param queueName
     * @param message
     * @throws Exception
     *//*
    public void sendMessage(String queueName, String message,String uuid) throws Exception {
    	logger.info("sendMessage start");
        declareBinding(queueName, queueName);
        CorrelationData correlationId = new CorrelationData(uuid);
        rabbitAdmin.getRabbitTemplate().convertAndSend(queueName, queueName, message,correlationId);
        logger.info("[rabbitmq-sendMessage]queueName:{} ,uuid:{},msg:{}",queueName,correlationId.getId(),message);
    }
   
}*/