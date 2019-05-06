package com.jinglitong.wallet.ddbkjserver.common.spring;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbRewardProcessSeq;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.ddbkjserver.service.MQTranService;
import com.jinglitong.wallet.ddbkjserver.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MQApplicationRunner {
	@Autowired
	private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;

	@Autowired
	private MQTranService mQTranService;

	@Value("${ali.accessKey}")
	private String accessKey;

	@Value("${ali.secretKey}")
	private String secretKey;

	@Value("${ali.server.addr}")
	private String serverAddr;

	@Value("${mq.tag.kj}")
	private String fhTag;

	@Value("${mq.topic}")
	private String consumerTopic;

	@Value("${ali.fhconsumer.id}")
	private String consumerId;

	@PostConstruct
	public void run()  {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, consumerId);
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		properties.put(PropertyKeyConst.ONSAddr, serverAddr);
		properties.put(PropertyKeyConst.SuspendTimeMillis, "100");
		properties.put(PropertyKeyConst.MaxReconsumeTimes, "20");
		OrderConsumer consumer = ONSFactory.createOrderedConsumer(properties);
		consumer.subscribe(consumerTopic, fhTag, new MessageOrderListener() {
			public OrderAction consume(Message message, ConsumeOrderContext context) {
				try {
					DdbConsumerHistory ddbConsumerHistory = ddbConsumerHistoryMapper.checkMessageId(message.getKey());
					if (ddbConsumerHistory == null) {
						String messageBody = new String(message.getBody());
						KJNotice notice = JSONObject.parseObject(messageBody, KJNotice.class);
						DdbRewardProcessSeq rewardSeq = addRewardProcessSeq(notice);

						/*DdbMqMessageRecord msg = new DdbMqMessageRecord(message.getKey(), consumerId, consumerTopic,
								fhTag, message.getMsgID(), 3, 2, DateUtils.getDateTime(), messageBody, true);*/
						DdbConsumerHistory ddbConsumerInsert = new DdbConsumerHistory();
						ddbConsumerInsert.setFlowId(message.getKey());
						ddbConsumerInsert.setMsgId(message.getMsgID());
						ddbConsumerInsert.setCreateTime(DateUtils.getDateTime());
						mQTranService.add(rewardSeq, ddbConsumerInsert);
						ConstantDict.ORDER_CONTROL.add(notice);
					} else {
						log.info("消息已处理过MessageId=" + message.getMsgID());
					}
					return OrderAction.Success;
				} catch (Exception e) {
					e.printStackTrace();
					return OrderAction.Suspend;
				}
			}
		});
		consumer.start();

	}

	private DdbRewardProcessSeq addRewardProcessSeq(KJNotice notice) {

		// ConstantDict.ORDER_CONTROL.add(notice);
		DdbRewardProcessSeq ddbRewardProcessSeq = null;
		if (notice.getType().equals("1")) {
			/*
			 * 新增分红订单流水表记录 分红对象类型=1订单 分红对象ZID=对应订单表flowid 状态=0未处理 创建时间=系统当前时间
			 */
			ddbRewardProcessSeq = new DdbRewardProcessSeq();
			ddbRewardProcessSeq.setType(1);
			ddbRewardProcessSeq.setOrderZid(notice.getFlowId());
			ddbRewardProcessSeq.setState(false);
			ddbRewardProcessSeq.setCreateTime(new Date());
			// ddbRewardProcessSeqMapper.insert(ddbRewardProcessSeq);

		} else if (notice.getType().equals("2")) {
			/*
			 * 新增分红订单流水表记录 分红对象类型=2育苗 分红对象ZID=育苗基地购买记录表zid 状态=0未处理 创建时间=系统当前时间
			 */
			ddbRewardProcessSeq = new DdbRewardProcessSeq();
			ddbRewardProcessSeq.setType(2);
			ddbRewardProcessSeq.setOrderZid(notice.getFlowId());
			ddbRewardProcessSeq.setState(false);
			ddbRewardProcessSeq.setCreateTime(new Date());
			// ddbRewardProcessSeqMapper.insert(ddbRewardProcessSeq);

		} else if (notice.getType().equals("3")) {
			// nothing
		}
		return ddbRewardProcessSeq;
	}
}
