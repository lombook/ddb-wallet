package com.jinglitong.springshop.mq;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.jinglitong.springshop.utils.UuidUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: AliMQServiceUtil.java
 * 作        者: yxl 2018年11月13日
 * 创建时间: 2018年11月13日
 * 功能说明:阿里reocketMQ发送消息工具类
 */
@Component
@Slf4j
public class AliMQServiceUtil {

	
	private static String accessKey;
	
	
	private static String secretKey;
	
	
	private static String serverAddr;
	
	
	@Value("${aliyun.mq.accessKey}")
	public  void setAccessKey(String accessKey) {
		AliMQServiceUtil.accessKey = accessKey;
	}
	@Value("${aliyun.mq.secretKey}")
	public  void setSecretKey(String secretKey) {
		AliMQServiceUtil.secretKey = secretKey;
	}
	@Value("${aliyun.mq.server.addr}")
	public  void setServerAddr(String serverAddr) {
		AliMQServiceUtil.serverAddr = serverAddr;
	}
	/**
	 * 
	 * 功能说明:全局消息 FIFO
	 * @param topic
	 * @param tag
	 * @param message
	 * @return
	 */
	public static SendResult sendByOrder(String orderId,String topic,String tag,String message,String groupName) {
		Properties properties = new Properties();
		// 您在控制台创建的 Producer ID	
		properties.put(PropertyKeyConst.GROUP_ID, groupName);		
		// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		// 设置 TCP 接入域名，进入 MQ 控制台的生产者管理页面，在左侧操作栏单击获取接入点获取
		// 此处以公共云生产环境为例
		properties.put(PropertyKeyConst.NAMESRV_ADDR, serverAddr);
		// http://onsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal
		OrderProducer producer = ONSFactory.createOrderProducer(properties);
		// 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可。
		producer.start();
		Message msg = new Message(topic, tag, message.getBytes());
		// 设置代表消息的业务关键属性，请尽可能全局唯一。
		// 以方便您在无法正常收到消息情况下，可通过 MQ 控制台查询消息并补发。
		// 注意：不设置也不会影响消息正常收发
		msg.setKey(orderId);
		// 分区顺序消息中区分不同分区的关键字段，sharding key 于普通消息的 key 是完全不同的概念。
		// 全局顺序消息，该字段可以设置为任意非空字符串。
		String shardingKey = String.valueOf(orderId);
		try {
			SendResult sendResult = producer.send(msg, shardingKey);
			// 发送消息，只要不抛异常就是成功
			if (sendResult != null) {
				log.info(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: "
						+ sendResult.getMessageId());
				producer.shutdown();
				return sendResult;
			}
		} catch (Exception e) {
			// 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
			log.info(new Date() + " Send mq message failed. Topic is:" + msg.getTopic());
			return null;
		}
		// 在应用退出前，销毁 Producer 对象
		// 注意：如果不销毁也没有问题
		producer.shutdown();
		return null;
	}

	public static String createNewOrder(String orderId, String message, String topic, String tag,String gropName) {
		String msgId = "";
		int count = 1;
		do {
			try {
				SendResult result = AliMQServiceUtil.sendByOrder(orderId, topic, tag, message,gropName);
				if (result != null) {
					msgId = result.getMessageId();
					log.info("ddbOrder =" + message + ",put MQ  success,count=" + count);
				} else {
					log.info("MQ send failure, try again,count=" + count);
				}
			} catch (Exception e) {
				log.info("MQ send failure, try again,count=" + count);
			} finally {
				count = count + 1;
			}
		} while (StringUtils.isEmpty(msgId) && count <= 5);

		return msgId;
	}

}
