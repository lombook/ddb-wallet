package com.jinglitong.wallet.give.mqservice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveDic;
import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveRecord;
import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveRule;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.give.mapper.DdbActiveGiveDicMapper;
import com.jinglitong.wallet.give.mapper.DdbActiveGiveRuleMapper;
import com.jinglitong.wallet.give.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.give.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.give.service.CalculateService;
import com.jinglitong.wallet.give.util.AliMQServiceUtil;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.MD5Utils;
import com.jinglitong.wallet.give.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: CalculateHdConsumer.java
 * 作        者: yxl 2018年11月7日
 * 创建时间: 2018年11月7日
 * 功能说明:从mq获取订单数据，对数据计算活动赠送，再放入mq中。
 */
@Service
@Slf4j
public class ActiveIntegConsumer {

	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	
	@Autowired
	private DdbActiveGiveRuleMapper ddbActiveGiveRuleMapper;
	
    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;
    
    @Autowired
    private CalculateService CalculateService;
    
    @Autowired
    private DdbActiveGiveDicMapper ddbActiveGiveDicMapper;
    
    @Value("${ali.product.id}")
    private String productId;
    
    @Value("${mq.topic}")
    private String topic;
    
    @Value("${ali.hdconsumer.id}")
    private String consumerId;
    
    @Value("${mq.tag.active}")
    private String hdTag;
    
    @Value("${mq.tag.active_http}")
    private String hdTag2;
    
    @Value("${mq.tag.give}")
    private String zsTag;
    
    @Value("${game.web.url}")
    private String gameWebUrl;
    
    @Value("${ali.accessKey}")
	private String accessKey;
	
    @Value("${ali.secretKey}")
	private String secretKey;
	
    @Value("${ali.server.addr}")
	private String serverAddr;
	
    @PostConstruct
    public void activeGive() {
    	
		Properties properties = new Properties();
		// 您在控制台创建的 Consumer ID
		properties.put(PropertyKeyConst.ConsumerId, consumerId);
		// AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.AccessKey, accessKey);
		// SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
		properties.put(PropertyKeyConst.SecretKey, secretKey);
		// 设置 TCP 接入域名，进入 MQ 控制台的消费者管理页面，在左侧操作栏单击获取接入点获取
		// 此处以公共云生产环境为例
		properties.put(PropertyKeyConst.ONSAddr, serverAddr);
		// 顺序消息消费失败进行重试前的等待时间，单位(毫秒)
		properties.put(PropertyKeyConst.SuspendTimeMillis, "100");
		// 消息消费失败时的最大重试次数
		properties.put(PropertyKeyConst.MaxReconsumeTimes, "20");
		// 在订阅消息前，必须调用 start 方法来启动 Consumer，只需调用一次即可。
		OrderConsumer consumer = ONSFactory.createOrderedConsumer(properties);
		consumer.subscribe(topic, hdTag, new MessageOrderListener() {
			public OrderAction consume(Message message, ConsumeOrderContext context) {
				try {
					log.info("HD messageId" + message.getMsgID() + ",message" + message.toString());
					String messageBody = new String(message.getBody());
					DdbConsumerHistory ddbConsumerHistory = ddbConsumerHistoryMapper.checkMessageId(message.getKey());
					if(ddbConsumerHistory == null){
						DdbOrder order = JSONObject.parseObject(messageBody, DdbOrder.class);
						DdbGoodsRule goodRule = new DdbGoodsRule();
						goodRule.setZid(order.getRuleId());
						// 获取商品的单价、active_group
						goodRule = ddbGoodsRuleMapper.selectOne(goodRule);
						if (StringUtils.isEmpty(goodRule.getActiveGroup())) {
							log.info("order:" + order.toString() + ",没有对应的活动规则");
							return OrderAction.Success;
						}
						// 获取活动规则
						Weekend<DdbActiveGiveRule>weekends = Weekend.of(DdbActiveGiveRule.class);
				    	WeekendCriteria<DdbActiveGiveRule,Object> Criterias =weekends.weekendCriteria();
				    	Criterias.andCondition(" hd_group = '"+goodRule.getActiveGroup()+"' and  effective_time <='"+order.getOrderCreateTime()+"' "
				    			+ "and expiry_time >= '"+order.getOrderCreateTime()+"'");
						//DdbActiveGiveRule activeRule = new DdbActiveGiveRule();
						//activeRule.setHdGroup(goodRule.getActiveGroup());
						DdbActiveGiveRule ddbActiveGiveRule = ddbActiveGiveRuleMapper.selectOneByExample(weekends);
						//for (DdbActiveGiveRule ddbActiveGiveRule : activeRuleList) {
							DdbActiveGiveRecord activeGiveRecord = new DdbActiveGiveRecord();
							activeGiveRecord.setZid(UuidUtil.getUUID());
							activeGiveRecord.setFlowId(order.getFlowId());
							activeGiveRecord.setShopTrade(order.getShopTrade());
							activeGiveRecord.setActiveRuleId(ddbActiveGiveRule.getZid());
							activeGiveRecord.setCustId(order.getUserId());
							Integer pp = Integer.valueOf(order.getProductNum()) * goodRule.getProductPrice();
							activeGiveRecord.setProductPrice(pp.longValue());
							activeGiveRecord.setPercent(ddbActiveGiveRule.getPercent());
							//String meiPricce = getMeiRealTime();
							//activeGiveRecord.setMeiPrice(meiPricce);
							activeGiveRecord.setInteName(ddbActiveGiveRule.getInteName());
							
							Weekend<DdbActiveGiveDic>weekend = Weekend.of(DdbActiveGiveDic.class);
					    	WeekendCriteria<DdbActiveGiveDic,Object> Criteria =weekend.weekendCriteria();
					    	Criteria.andCondition(" rule_id='"+order.getRuleId()+"' and effective_time <= '"+order.getOrderCreateTime()+"'"
					    			+ " and expiry_time >='"+order.getOrderCreateTime()+"' ");
					    	DdbActiveGiveDic ddbActiveGiveDic= ddbActiveGiveDicMapper.selectOneByExample(weekend);
							int inteAmount = 
							calculate(ddbActiveGiveDic.getProductPrice().intValue()*Integer.valueOf(order.getProductNum()),
									 ddbActiveGiveRule.getPercent());
							activeGiveRecord.setInteAmount(String.valueOf(inteAmount));
							activeGiveRecord.setRate(ddbActiveGiveRule.getRate().stripTrailingZeros().toPlainString());

							String time = DateUtils.getDateTime();
							activeGiveRecord.setCreateTime(time);
							activeGiveRecord.setUpdateTime(time);
							DdbMqMessageRecord putMsg =pushActiveDataToGame(activeGiveRecord, inteAmount, 
									ddbActiveGiveRule.getHdGroup());
							if(putMsg != null && !StringUtils.isEmpty(putMsg.getMsgId())) {
								activeGiveRecord.setStatus(1);
							}else {
								activeGiveRecord.setStatus(0);
							}
							 DdbConsumerHistory ddbConsumerInsert = new DdbConsumerHistory();
							 ddbConsumerInsert.setFlowId(message.getKey());
		                     ddbConsumerInsert.setMsgId(message.getMsgID());
		                     ddbConsumerInsert.setCreateTime(DateUtils.getDateTime());
		                     List<DdbMqMessageRecord> listMsg = new ArrayList<DdbMqMessageRecord>();
		                     listMsg.add(putMsg);
		                     CalculateService.add(activeGiveRecord, listMsg, ddbConsumerInsert);
						//}
					}else {
						log.info("HD exist  messageId" + message.getMsgID() + ",message" + message.toString());
	                }
					log.info("HD Success  messageId" + message.getMsgID() + ",message" + message.toString());
					return OrderAction.Success;
				} catch (Exception e) {
					log.info("HD Suspend  messageId" + message.getMsgID() + ",message" + message.toString()+",consumeTimes:"+message.getReconsumeTimes());
					return OrderAction.Suspend;
				}
				 
			}
		});
		consumer.start();
	}
	/**
	 * 
	 * 功能说明:同步活动赠送的积分给游戏方
	 * @param activeGiveRecord
	 * @throws Exception 
	 */
	private DdbMqMessageRecord pushActiveDataToGame(DdbActiveGiveRecord activeGiveRecord,int inteAmount,String hdGroup){
		DdbMqMessageRecord msg = null ;
		Map<String, String> data = new HashMap<>();
		try {
			Map<String, Object> param = new HashMap<>();
			String flowId = UuidUtil.getUUID();
			param.put("flowId", flowId);// 流水号
			param.put("userId", activeGiveRecord.getCustId());// 用戶id
			param.put("shopTrade", activeGiveRecord.getShopTrade());// 订单号
			param.put("baoCoin", inteAmount);// 积分数量
			param.put("hdruleId", hdGroup);// 活动规则id
			param.put("nonceStr", Math.round(Math.random() * 10) + 1);// 随机数
			String key  = MD5Utils.makeSign(JSON.toJSONString(param));
			param.put("sign", key);// 加密串
	        
	        data.put("address", gameWebUrl);
	        data.put("body", JSON.toJSONString(param));
	        data.put("flowId", flowId);
			log.info("twoStep put data:{}", JSON.toJSONString(data));
			String msgId = AliMQServiceUtil.createNewOrder(flowId,JSON.toJSONString(data), topic, hdTag2);
		    msg = new DdbMqMessageRecord(flowId,productId, topic, hdTag2,
		    		msgId, 3, 1, DateUtils.getDateTime(), JSON.toJSONString(data), true);
		    if(StringUtils.isEmpty(msgId)) {
		    	msg.setStatus(false);
		    }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return msg;
	}
	/**
	 * 
	 * 功能说明:从交易所获取MEI实时价格，目前是5分 httpPost
	 * @return
	 */
	private String getMeiRealTime() {
        return "5";
	}
	
	public Integer calculate(int price,BigDecimal percent) {
		 //divide(new BigDecimal(mei), 2, RoundingMode.DOWN);
		BigDecimal a = new BigDecimal(price).multiply(percent);
		log.info("calculate result{} price{},percent{} ",a.intValue(),price,percent);
		return a.intValue();
	}
	
	public static void main(String[] args) {
		
	}
}
