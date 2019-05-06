/*package com.jinglitong.wallet.give.service;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.feign.NoticeKJFeignApi;
import com.jinglitong.wallet.ddbapi.model.DdbControlRule;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.give.common.storage.AliCloudMailService;
import com.jinglitong.wallet.give.mapper.CustomerMapper;
import com.jinglitong.wallet.give.mapper.DdbControlRuleMapper;
import com.jinglitong.wallet.give.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.give.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.give.mapper.DdbOrderMapper;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.RocketMQUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class OrderService {
   *//**
	 * 重试次数
	 *//*
	static int count;
	

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RequireHistoryService requireHistoryService;
    @Autowired
    private DdbOrderMapper ddbOrderMapper;
    @Autowired
    private SplitOrderService splitOrderService;
    @Autowired
    private NoticeKJFeignApi noticeKJFeignApi;

	@Autowired
	private AliCloudMailService aliCloudMailService;
    
	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper; 
	
	@Autowired
	private DdbControlRuleMapper ddbControlRuleMapper;
	
	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	
    *//**
     * 消费者的组名
     *//*
    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;

    *//**
     * 消费者的组名
     *//*
    @Value("${apache.rocketmq.product.producer}")
    private String producerGroup;

    *//**
     * NameServer 地址
     *//*
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${product.fail.address}")
    private String address;
    
    @Value("${mq.hdgroup.name}")
    private String mqHdgroupName;

    @Value("${mq.hdgroup.name}")
    private String mqFhgroupName;

    public boolean orderHandle(Map<String, Object> map) {
        String  flowId = (String)map.get("flowId");
        String  userId = (String)map.get("userId");
        Customer customer = customerMapper.selectByCustId(userId);
        if(customer == null){
            log.info("没有该用户信息custId："+userId);
            return false;
        }





        //排重入库
        requireHistoryService.ddbRequireHistoryInsert(flowId, DateUtils.getDateTime());

        //订单插入ddbOrder表
        DdbOrder ddbOrder = splitOrderService.insertDdbOrder(map);
        //活动赠送
        DdbGoodsRule goodRule = new DdbGoodsRule();
		goodRule.setZid(ddbOrder.getRuleId());
		goodRule = ddbGoodsRuleMapper.selectOne(goodRule);
		if (!StringUtils.isEmpty(goodRule.getActiveGroup())) {
			DdbControlRule hd = new DdbControlRule();
			hd.setGroupName(mqHdgroupName);
			hd = ddbControlRuleMapper.selectOne(hd);
			createNewOrder(ddbOrder, hd);
		}
       
        activeGiveService.activeGive(ddbOrder);
        DdbOrder update = new DdbOrder();
        update.setFlowId(ddbOrder.getFlowId());
        update.setActiveState(true);
    	Weekend<DdbOrder>weekend = Weekend.of(DdbOrder.class);
    	WeekendCriteria<DdbOrder,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andEqualTo(DdbOrder::getFlowId, ddbOrder.getFlowId());
        ddbOrderMapper.updateByExampleSelective(update, weekend);

        KJNotice kjNotice = new KJNotice();
        kjNotice.setCustId(ddbOrder.getUserId());
        kjNotice.setFlowId(ddbOrder.getFlowId());
        kjNotice.setProductNum(ddbOrder.getProductNum());
        kjNotice.setRuleId(ddbOrder.getRuleId());
        kjNotice.setShopTrade(ddbOrder.getShopTrade());
        kjNotice.setId(ddbOrder.getId());
        kjNotice.setType("1");
        try {
            Map<String, Object> result = noticeKJFeignApi.order(kjNotice);
            if(!result.get("code").equals(0)){
                log.error("send to kj  server err ",kjNotice);
            }else {
                log.info("send to kj server result {}", result.toString());
            }
        }catch (Exception e){
            log.error("send to kj server err ",e);
        }


        try{
            //拆分订单转账
            splitOrderService.splitOrder(ddbOrder);
            //修改订单状态
            ddbOrderMapper.updateStateByFlowId(1,ddbOrder.getFlowId());
       }catch (RuntimeException e){
            log.info("flowId"+ddbOrder.getFlowId()+".订单处理异常"+e);
        }

        return true;
    }


    public  boolean createNewOrder(DdbOrder ddbOrder,DdbControlRule hd) {
	    count = 0;
		RetryTemplate retryTemplate = new RetryTemplate();
        // 设置重试策略，主要设置重试次数
        SimpleRetryPolicy policy = new SimpleRetryPolicy(10, Collections.<Class<? extends Throwable>, Boolean>singletonMap(Exception.class, true));
        // 设置重试回退操作策略，主要设置重试间隔时间
        ExponentialBackOffPolicy fixedBackOffPolicy = new ExponentialBackOffPolicy();

        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        // 通过RetryCallback 重试回调实例包装正常逻辑逻辑，第一次执行和重试执行执行的都是这段逻辑
        final RetryCallback<Object, Exception> retryCallback = new RetryCallback<Object, Exception>() { //RetryContext 重试操作上下文约定，统一spring-try包装
           @Override
            public Object doWithRetry(RetryContext context) throws Exception {
                try {
                    Message message = new Message(hd.getTopic(), hd.getTag(), JSON.toJSONString(ddbOrder).getBytes());
                    SendResult result = RocketMQUtil.simpleProvider(namesrvAddr, producerGroup, message);
                    if(!"SEND_OK".equals(result.getSendStatus().toString())){
                        throw new RuntimeException();//这个点特别注意，重试的根源通过Exception返回
                    }else {
                    	DdbMqMessageRecord msg = new DdbMqMessageRecord();
                    	msg.setGroupName(producerGroup);
                    	msg.setTopic(hd.getTopic());
                    	msg.setTag(hd.getTag());
                    	msg.setMsgId(result.getMsgId());
                    	msg.setDataBody(JSON.toJSONString(ddbOrder));
                    	msg.setSendType(3);
                    	msg.setGroupType(1);
                    	msg.setCreateTime(DateUtils.getDateTime());
    					ddbMqMessageRecordMapper.insert(msg);
    					log.info("ddbOrder flowId="+ddbOrder.getFlowId()+",put MQ  success");
                    }
                } catch (Exception e) {
                	count = count +1;
                	System.out.println(count);
                    log.info("MQ send failure, try again");
                    throw new RuntimeException();//这个点特别注意，重试的根源通过Exception返回
                }
                return true;
            }
        };
        // 通过RecoveryCallback 重试流程正常结束或者达到重试上限后的退出恢复操作实例
        final RecoveryCallback<Object> recoveryCallback = new RecoveryCallback<Object>() {
            @Override
            public Object recover(RetryContext context) throws Exception { //                logger.info("正在重试发券::::::::::::"+userId);
            return null;
        } };
        try { // 由retryTemplate 执行execute方法开始逻辑执行
            retryTemplate.execute(retryCallback, recoveryCallback);
        } catch (Exception e) { //            logger.info("发券错误异常========"+e.getMessage());
            e.printStackTrace();
            return false;
        }
        if(count < 10) {
        	return true;
        }else {
        	aliCloudMailService.sendMail(address,  "活动赠送放入MQ第一步失败","活动赠送放入MQ失败十次，"
        			+ "查看表ddbOrder,flowId="+ddbOrder.getFlowId());
        	log.info("ddbOrder flowId="+ddbOrder.getFlowId()+", error:put MQ  failure");
        }
		return false;
	}
}
*/