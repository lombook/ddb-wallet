package com.jinglitong.springshop.controller;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.springshop.entity.Currency;
import com.jinglitong.springshop.entity.MqConsumerHistory;
import com.jinglitong.springshop.entity.Paymenttransaction;
import com.jinglitong.springshop.mapper.CurrencyMapper;
import com.jinglitong.springshop.mapper.MqConsumerHistoryMapper;
import com.jinglitong.springshop.mapper.PaymenttransactionMapper;
import com.jinglitong.springshop.service.KukaOrderRewardService;
import com.jinglitong.springshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
/**
 * 当支付法币金额为零时，接收mq消息，设置支付成功
 * @author prh10
 *
 */

@Component
@Slf4j
public class PayBackMqConsumer {

	@Autowired
	private PaymenttransactionMapper paymentTransactionMapper;

    @Value("${aliyun.mq.accessKey}")
	private String accessKey;
	
    @Value("${aliyun.mq.secretKey}")
	private String secretKey;
	
    @Value("${aliyun.mq.server.addr}")
	private String serverAddr;
    
    @Value("${aliyun.mq.order.nonepaynotice.group}")
    private String consumerId;
    
    @Value("${aliyun.mq.order.topic}")
    private String topic;
    
    @Value("${aliyun.mq.order.nonepaynotice.tag}")
    private String tag;
        
    
	@Autowired
	private MqConsumerHistoryMapper mqConsumerHistoryMapper;
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private KukaOrderRewardService kukaOrderRewardService;
	
	
	@Autowired
	private CurrencyMapper currencyMapper;
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
		properties.put(PropertyKeyConst.SuspendTimeMillis, "100");
		// 消息消费失败时的最大重试次数
		properties.put(PropertyKeyConst.MaxReconsumeTimes, "20");
		// 在订阅消息前，必须调用 start 方法来启动 Consumer，只需调用一次即可。
		OrderConsumer consumer =  ONSFactory.createOrderedConsumer(properties);
		consumer.subscribe(topic, tag, new MessageOrderListener() {
			public OrderAction consume(Message message, ConsumeOrderContext context) {
				try {
					log.info("HD messageId" + message.getMsgID() + ",message" + message.toString());
					String messageBody = new String(message.getBody());
					MqConsumerHistory history = new MqConsumerHistory();
					history.setTag(tag);
					history.setFlowId(message.getKey());
					history = mqConsumerHistoryMapper.selectOne(history);
					if (history != null) {
						log.info("ConsumerHistory 已有数据");
						return OrderAction.Success;
					} else {
						JSONObject obj = JSON.parseObject(messageBody);
						;
						MqConsumerHistory insert = new MqConsumerHistory();
						insert.setFlowId(message.getKey());
						insert.setMsgId(message.getMsgID());
						insert.setTag(tag);
						insert.setCreateTime(new Date());
						
						//根据币种分别处理微信支付回调和支付宝支付回调
						Paymenttransaction p = new Paymenttransaction();
						p.setZid(obj.getString("out_order_no"));
						p = paymentTransactionMapper.selectOne(p);
						if(p == null){
							return OrderAction.Suspend;
						}
						Currency c = new Currency();
						c.setZid(p.getCurrencyId());
						c = currencyMapper.selectOne(c);
						int res = 0;
						if(c.getCurrencycode().equals("CNY")){
							res = aliPayback(obj.getString("out_order_no"));
						}						
						if( res == 1){
							mqConsumerHistoryMapper.insert(insert);
							log.info("HD Success  messageId" + message.getMsgID() + ",message" + message.toString());
							return  OrderAction.Success;
						}else{
							return OrderAction.Suspend;
						}	
					}
				} catch (Exception e) {
					log.info("HD Suspend  messageId" + message.getMsgID() + ",message" + message.toString()
							+ ",consumeTimes:" + message.getReconsumeTimes());
					return OrderAction.Suspend;
				}

			}
		});
		consumer.start();
	} 
	
	private int aliPayback(String out_trade_no) {
		Paymenttransaction p = new Paymenttransaction();
		p.setZid(out_trade_no);
		p = paymentTransactionMapper.selectOne(p);
		if (p == null) {
			log.error("内部支付回调参数错误，找不到对应的支付事务zid：{}", out_trade_no);
			return 0;
		}
		if (p.getPaystatus().equals(1)) {
			// 如果时已支付状态，直接返回成功
			return 1;
		}

		/*if (p.getAmount().compareTo(new BigDecimal(0)) != 0) {
			log.error("{}支付金额不正确！", p.getZid());
			return 0;
		}*/

		orderService.dealAfterPaySuccess(p, 0);
		// 支付完成之后给用户自己赠送带金币
		kukaOrderRewardService.giveOrderReward(p.getOrderId());
		return 1;
	}
}
