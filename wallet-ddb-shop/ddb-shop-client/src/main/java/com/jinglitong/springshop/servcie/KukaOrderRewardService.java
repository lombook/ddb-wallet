package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.entity.MqMessageRecord;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.util.AliMQServiceUtil;
import com.jinglitong.springshop.vo.ShopOrderRewardVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class KukaOrderRewardService {
	
	@Autowired
    private OrderService orderService;
    
    @Autowired
    private OrdersMapper ordersMapper;
	
    
    @Autowired
	private MqMessageRecordService mqMessageRecordService;
    
    /**
     * 国外商品订单完成时发送mq groupID
     */
    @Value("${aliyun.mq.order.rewardproduct}")
    private String rewardPoducerId;
    
    /**
     * 国内商品订单完成时发送mq groupID
     */
    @Value("${aliyun.mq.order.chinarewardproduct}")
    private String chinarewardPoducerId;
    
    @Value("${aliyun.mq.order.topic}")
    private String topic;
    
    /**
     * 国外商品订单完成时发送mq tag
     */
    @Value("${aliyun.mq.orderreward.tag}")
    private String rewardtag;
    
    /**
     * 国内商品订单完成时发送mq tag
     */
    @Value("${aliyun.mq.orderreward.chinatag}")
    private String chinarewardtag;         
    
    /**
     * 订单完成后向kj发送mq消息.区分国内国外
     * @param orderId 主订单id
     */
    public void sendMqToKj(String orderId){
    	//根据订单币种 判断时国内商品还是国外商品，国内商品和国外商品发送mq参数不同，发送内容不同
    	String tag = null;
    	String groupId = null;
    	String currencyCode = orderService.getCurrencyCode(orderId);
    	if(currencyCode.equals("CNY")){
    		tag = rewardtag;
    		groupId = rewardPoducerId;
    		try {
    			int historyCount = mqMessageRecordService.getMqRecordCount(orderId, tag);
    			if(historyCount == 0) {
    				ShopOrderRewardVo ShopOrderRewardVo = getShopOrderReward(orderId);
    				log.info("put reward data to Mq start");
    				//ProductRabbitMqService.sendMessage(RabbitConstant.ORDER_QUEUE_NAME, JSON.toJSONString(param),uuid);
    				String hdResult =AliMQServiceUtil.createNewOrder(orderId, JSON.toJSONString(ShopOrderRewardVo),topic, tag,groupId);
    				log.info("put reward data to Mq end");
    				MqMessageRecord insert = buidBean(orderId,hdResult,tag,JSON.toJSONString(ShopOrderRewardVo),groupId);
    				if(StringUtils.isEmpty(hdResult)) {
    					insert.setStatus(false);
    				}else {
    					insert.setStatus(true);
    				}
    				mqMessageRecordService.insertRecord(insert);
    				log.info("have put reward data to Mq");
    			}else {
    				log.info("订单{}已处理reward",orderId);
    			}
    		} catch (Exception e) {
    			log.info("订单{}处理reward异常",orderId);
    			e.printStackTrace();
    		}
    	}/*else if(currencyCode.equals("CNY")){   		
    		tag = chinarewardtag;
    		groupId = chinarewardPoducerId;
    		try {
    			int historyCount = mqMessageRecordService.getMqRecordCount(orderId, tag);
    			if(historyCount == 0) {
    				log.info("put reward data to Mq start");
    				Map<String,String> putdata = new HashMap<String,String>();
    				putdata.put("orderId", orderId);
    				String hdResult =AliMQServiceUtil.createNewOrder(orderId, JSON.toJSONString(putdata),topic, tag,groupId);
    				log.info("put reward data to Mq end");
    				MqMessageRecord insert = buidBean(orderId,hdResult,tag,JSON.toJSONString(putdata),groupId);
    				if(StringUtils.isEmpty(hdResult)) {
    					insert.setStatus(false);
    				}else {
    					insert.setStatus(true);
    				}
    				mqMessageRecordService.insertRecord(insert);
    				log.info("have put reward data to Mq");
    			}else {
    				log.info("订单{}已处理reward",orderId);
    			}
    		} catch (Exception e) {
    			log.info("订单{}处理reward异常",orderId);
    			e.printStackTrace();
    		}
    	}*/
		
	}	
	//根据主订单获得KukaOrderRewardVo对象
	private ShopOrderRewardVo getShopOrderReward(String orderId){
		//根据主订单获得所有子订单，每个子订单生成一个KukaOrderReward
		Orders order = new Orders();
		order.setZid(orderId);
		order = ordersMapper.selectOne(order);
		BigDecimal sValue = BigDecimal.ZERO;
		BigDecimal djbValue = BigDecimal.ZERO;
		if(order.getSValue() != null){
			sValue = order.getSValue();
		}		
		if(order.getDjbValue() != null){
			djbValue = order.getDjbValue();
		}
		
		ShopOrderRewardVo shopOrderRewardVo = bulidShopOrderRewardVo(order.getCustId(),order.getZid(),order.getSn(),order.getCreateTime(),sValue,djbValue);
		return shopOrderRewardVo;
	}
	
	private ShopOrderRewardVo bulidShopOrderRewardVo(String userId, String flowId, String orderSn, Date orderCreateTime, BigDecimal sValue, BigDecimal djbValue){
		ShopOrderRewardVo shopOrderReward = new ShopOrderRewardVo();
		shopOrderReward.setFlowId(flowId);
		shopOrderReward.setCustId(userId);
		shopOrderReward.setOrderSn(orderSn);
		shopOrderReward.setOrderCreateTime(orderCreateTime);
		shopOrderReward.setsValue(sValue);
		shopOrderReward.setDjbValue(djbValue);
		return shopOrderReward;
	}
		
	 private MqMessageRecord buidBean (String uuid,String result ,String tag,String o,String groupId) {
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
