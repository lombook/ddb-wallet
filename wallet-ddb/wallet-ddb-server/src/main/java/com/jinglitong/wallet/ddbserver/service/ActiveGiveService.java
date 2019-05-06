package com.jinglitong.wallet.ddbserver.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveRecord;
import com.jinglitong.wallet.ddbapi.model.DdbActiveGiveRule;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbMqMessageRecord;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudMailService;
import com.jinglitong.wallet.ddbserver.mapper.DdbActiveGiveRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbActiveGiveRuleMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.HttpUtil;
import com.jinglitong.wallet.ddbserver.util.MD5Utils;
import com.jinglitong.wallet.ddbserver.util.RocketMQUtil;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ActiveGiveService.java
 * 作        者: yxl 2018年10月25日
 * 创建时间: 2018年10月25日
 * 功能说明:活动赠送积分-宝分
 */
@Service
@Slf4j
public class ActiveGiveService {

	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	
	@Autowired
	private DdbActiveGiveRuleMapper ddbActiveGiveRuleMapper;
	
	@Autowired
	private DdbActiveGiveRecordMapper ddbActiveGiveRecordMapper;
	
	@Autowired
	private AliCloudMailService aliCloudMailService;
	
	@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper; 
	
	/**
	 * 重试次数
	 */
	static int count;
	
	/**
     * 生产者的组名
     */
    //@Value("${apache.rocketmq.product.producer}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
   // @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;


    //@Value("${product.fail.address}")
    private String address;
	
	@Transactional(propagation =Propagation.REQUIRES_NEW)
	public boolean activeGive(DdbOrder order) {
		try {
			DdbGoodsRule goodRule = new DdbGoodsRule();
			goodRule.setZid(order.getRuleId());
			//获取商品的单价、active_group
			goodRule = ddbGoodsRuleMapper.selectOne(goodRule);
			if(StringUtils.isEmpty(goodRule.getActiveGroup())) {
				log.info("order:"+order.toString()+",没有对应的活动规则");
				return false;
			}
			//获取活动规则
			DdbActiveGiveRule activeRule = new DdbActiveGiveRule();
			activeRule.setHdGroup(goodRule.getActiveGroup());
			List<DdbActiveGiveRule> activeRuleList =ddbActiveGiveRuleMapper.select(activeRule);
			for (DdbActiveGiveRule ddbActiveGiveRule : activeRuleList) {
				DdbActiveGiveRecord activeGiveRecord = new DdbActiveGiveRecord();
				activeGiveRecord.setZid(UuidUtil.getUUID());
				activeGiveRecord.setFlowId(order.getFlowId());
				activeGiveRecord.setShopTrade(order.getShopTrade());
				activeGiveRecord.setActiveRuleId(ddbActiveGiveRule.getZid());
				activeGiveRecord.setCustId(order.getUserId());
				activeGiveRecord.setProductPrice(Integer.valueOf(order.getProductNum()) * goodRule.getProductPrice().longValue());
				activeGiveRecord.setPercent(ddbActiveGiveRule.getPercent());
				String meiPricce =getMeiRealTime();
				activeGiveRecord.setMeiPrice(meiPricce);
				activeGiveRecord.setInteName(ddbActiveGiveRule.getInteName());
				int inteAmount = activeGiveRecord.getProductPrice().intValue() * (100/(Integer.valueOf(meiPricce))) 
						* ddbActiveGiveRule.getPercent().intValue();
				activeGiveRecord.setInteAmount(String.valueOf(inteAmount));
				activeGiveRecord.setRate(ddbActiveGiveRule.getRate().stripTrailingZeros().toPlainString());
				
				String time = DateUtils.getDateTime();
				activeGiveRecord.setCreateTime(time);
				activeGiveRecord.setUpdateTime(time);
				boolean flag =pushActiveDataToGame(activeGiveRecord,inteAmount,ddbActiveGiveRule.getHdGroup());
				if(flag) {
					activeGiveRecord.setStatus(1);
				}else{
					activeGiveRecord.setStatus(0);
					aliCloudMailService.sendMail(address,  "活动赠送放入MQ失败","活动赠送放入MQ失败十次，查看表DdbActiveGiveRecord,flowId="+order.getFlowId());
				}
				ddbActiveGiveRecordMapper.insert(activeGiveRecord);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * 功能说明:同步活动赠送的积分给游戏方
	 * @param activeGiveRecord
	 * @throws Exception 
	 */
	private boolean pushActiveDataToGame(DdbActiveGiveRecord activeGiveRecord,int inteAmount,String hdGroup){
	    count = 0;
		Map<String, Object> param = new HashMap<>();
		String flowId = UuidUtil.getUUID();
		param.put("flowId", flowId);// 流水号
		param.put("userId", activeGiveRecord.getCustId());// 用戶id
		param.put("shopTrade", activeGiveRecord.getShopTrade());// 订单号
		param.put("baoCoin", inteAmount);// 积分数量
		param.put("hdruleId", hdGroup);// 活动规则id
		param.put("nonceStr", Math.round(Math.random() * 10) + 1);// 随机数
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
                	String key = MD5Utils.makeSign(JSON.toJSONString(param));
            		param.put("sign", key);// 加密串
            		log.info("调用游戏接口参数param:{}", key);
                    Map<String, String> data = new HashMap<>();
                    data.put("address", "http://dadibao.fudeshu.com/app/ddbact/givebf");
                    data.put("body", JSON.toJSONString(param));
                    data.put("flowId", flowId);
                    System.out.println("给游戏的"+JSON.toJSONString(param));
                    Message message = new Message("", "", JSON.toJSONString(data).getBytes());
                    SendResult result = RocketMQUtil.orderProvider(namesrvAddr, producerGroup, message, 1);
                    if(!"SEND_OK".equals(result.getSendStatus().toString())){
                        throw new RuntimeException();//这个点特别注意，重试的根源通过Exception返回
                    }else {
                    	DdbMqMessageRecord msg = new DdbMqMessageRecord();
                    	msg.setGroupName(producerGroup);
                    	msg.setTopic("");
                    	msg.setTag("");
                    	msg.setMsgId(result.getMsgId());
                    	msg.setDataBody(JSON.toJSONString(data));
                    	msg.setSendType(3);
                    	msg.setGroupType(1);
                    	msg.setCreateTime(DateUtils.getDateTime());
    					ddbMqMessageRecordMapper.insert(msg);
                    }
                } catch (Exception e) {
                	count = count +1;
                	System.out.println(count);
                    log.info("MQ发送失败，进行重试");
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
        }
		return false;
	}
	/**
	 * 
	 * 功能说明:从交易所获取MEI实时价格，目前是5分 httpPost
	 * @return
	 */
	private String getMeiRealTime() {
		
		return "5";
	}
	
}
