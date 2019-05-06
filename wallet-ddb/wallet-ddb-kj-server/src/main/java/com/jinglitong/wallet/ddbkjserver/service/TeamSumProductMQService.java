package com.jinglitong.wallet.ddbkjserver.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbOrderMapper;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardProcessSeqMapper;
import com.jinglitong.wallet.ddbkjserver.util.AliMQServiceUtil;
import com.jinglitong.wallet.ddbkjserver.util.DateUtils;
import com.jinglitong.wallet.ddbkjserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TeamSumProductMQService {

	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper;

	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	
	@Autowired
	private DdbOrderMapper ddbOrderMapper;

	@Value("${mq.topic}")
	private String topic;

	@Value("${mq.tag.teamsum}")
	private String TSTag;

	@Value("${ali.product.id}")
	private String producerId;

	public void sendTeamSumToMq(String custId, KJNotice order, String actionTime) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		map.put("custId", custId);
		// 获取 订单金额
		BigDecimal orderAmount = order.getAmount();
		map.put("orderAmount", orderAmount.toString());
		// 查询订单生成时间
		DdbOrder ddbOrder = new DdbOrder();
		ddbOrder.setFlowId(order.getFlowId());
		ddbOrder = ddbOrderMapper.selectOne(ddbOrder);
		String orderCreateTime = ddbOrder.getOrderCreateTime();
		map.put("orderDate", orderCreateTime);
		// 团队业绩发送到mq并且入库，同一笔订单要求入库时间一样，所以生成一个公共时间
		map.put("actionTime", actionTime);
		map.put("shopTrade", order.getShopTrade());

		String TSId = UuidUtil.getUUID();
		String TSResult = AliMQServiceUtil.createNewOrder(TSId, JSON.toJSONString(map), topic, TSTag);
		DdbMqMessageRecord TSMsg = buidBean(TSId, TSResult, TSTag, JSON.toJSONString(map));
		if (!StringUtils.isEmpty(TSResult)) {
			TSMsg.setStatus(true);
		} else {
			TSMsg.setStatus(false);
			log.info("TeamSum flowId=" + TSMsg.getFlowId() + ", error:hd put MQ oneStep failure");
		}
		ddbMqMessageRecordMapper.insert(TSMsg);

	}

	public DdbMqMessageRecord buidBean(String uuid, String result, String tag, Object o) {
		DdbMqMessageRecord msg = new DdbMqMessageRecord();
		msg.setFlowId(uuid);
		msg.setGroupName(producerId);
		msg.setTopic(topic);
		msg.setTag(tag);
		msg.setMsgId(result);
		msg.setDataBody(JSON.toJSONString(o));
		msg.setSendType(3);
		msg.setGroupType(1);
		msg.setCreateTime(DateUtils.getDateTime());
		return msg;
	}
}
