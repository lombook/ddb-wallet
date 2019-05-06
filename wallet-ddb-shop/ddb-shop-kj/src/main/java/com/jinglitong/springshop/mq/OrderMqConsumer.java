package com.jinglitong.springshop.mq;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSONObject;
import com.jinglitong.springshop.service.AliCloudMailsService;
import com.jinglitong.springshop.service.OrderRewardService;
import com.jinglitong.springshop.vo.ShopOrderRewardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.springshop.entity.MqConsumerHistory;
import com.jinglitong.springshop.mapper.MqConsumerHistoryMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * Copyright (c) 2019, 井立通 All rights reserved. 文件名称: OrderConsumer.java 作 者:
 * yxl 2019年1月24日 创建时间: 2019年1月24日 功能说明:从mq获取订单号，处理后发送给大地宝
 */
@Component
@Slf4j
public class OrderMqConsumer {

	@Value("${aliyun.mq.accessKey}")
	private String accessKey;

	@Value("${aliyun.mq.secretKey}")
	private String secretKey;

	@Value("${aliyun.mq.server.addr}")
	private String serverAddr;

	@Value("${aliyun.mq.order.consumer}")
	private String consumerId;

	@Value("${aliyun.mq.order.topic}")
	private String topic;

	@Value("${aliyun.mq.orderreward.tag}")
	private String tag;

	private String suspendTimeMillis="100";

	private String maxReconsumeTimes="10";

	@Value("${orderReward_email_address}")
	private String orderReward_email_address;

	@Autowired
	private MqConsumerHistoryMapper mqConsumerHistoryMapper;
	@Autowired
	private OrderRewardService orderRewardService;
	@Autowired
	private AliCloudMailsService aliCloudMailsService;

	@PostConstruct
	public void activeGive() {

		Properties properties = new Properties();
		// 您在控制台创建的 Consumer ID
		properties.put(PropertyKeyConst.GROUP_ID, consumerId);
		// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		// 设置 TCP 接入域名，进入 MQ 控制台的消费者管理页面，在左侧操作栏单击获取接入点获取
		// 此处以公共云生产环境为例
		properties.put(PropertyKeyConst.NAMESRV_ADDR, serverAddr);
		// 顺序消息消费失败进行重试前的等待时间，单位(毫秒)
		properties.put(PropertyKeyConst.SuspendTimeMillis, suspendTimeMillis);
		// 消息消费失败时的最大重试次数
		properties.put(PropertyKeyConst.MaxReconsumeTimes, maxReconsumeTimes);
		// 在订阅消息前，必须调用 start 方法来启动 Consumer，只需调用一次即可。
		OrderConsumer consumer =  ONSFactory.createOrderedConsumer(properties);
		consumer.subscribe(topic, tag, new MessageOrderListener() {
			public OrderAction consume(Message message, ConsumeOrderContext context) {
				try {
					log.info("HD messageId" + message.getMsgID() + ",message" + message.toString());
					String messageBody = new String(message.getBody());
					MqConsumerHistory history = new MqConsumerHistory();
					history.setFlowId(message.getKey());
					history.setTag(message.getTag());
					history = mqConsumerHistoryMapper.selectOne(history);
					if (history != null) {
						log.error("ConsumerHistory 已有数据");
						return OrderAction.Success;
					} else {
						MqConsumerHistory insert = new MqConsumerHistory();
						insert.setFlowId(message.getKey());
						int consumeCount = message.getReconsumeTimes();
						if(consumeCount>=Integer.parseInt(maxReconsumeTimes)){
							aliCloudMailsService.sendMail(orderReward_email_address,"大地宝现贝邀请订单奖励异常","大地宝现贝邀请订单奖励已超过最大发送次数，flowId:"+message.getKey());
						}
						insert.setMsgId(message.getMsgID());
						insert.setCreateTime(new Date());
						insert.setTag(message.getTag());
						ShopOrderRewardVo shopOrderRewardVo = (ShopOrderRewardVo) JSONObject.parseObject(messageBody, ShopOrderRewardVo.class);
						orderRewardService.SaleRewardAndTransfer(shopOrderRewardVo);
						mqConsumerHistoryMapper.insert(insert);
					}
					return OrderAction.Success;
				} catch (Exception e) {
					log.error("HD Suspend  messageId" + message.getMsgID() + ",message" + message.toString()
							+ ",consumeTimes:" + message.getReconsumeTimes());
					return OrderAction.Suspend;
				}

			}
		});
		consumer.start();
	}
}
