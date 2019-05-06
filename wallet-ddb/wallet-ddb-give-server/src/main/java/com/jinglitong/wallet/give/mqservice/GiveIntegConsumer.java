package com.jinglitong.wallet.give.mqservice;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.give.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.give.mapper.DdbMqMessageRecordMapper;
import com.jinglitong.wallet.give.service.SplitOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.Properties;


@Controller
@Slf4j
public class GiveIntegConsumer {

    @Value("${ali.zsconsumer.id}")
    private String consumerId;

    @Value("${ali.accessKey}")
    private String accessKey;

    @Value("${ali.secretKey}")
    private String secretKey;

    @Value("${ali.server.addr}")
    private String serverAddr;

    @Value("${mq.topic}")
    private String consumerTopic;

    @Value("${mq.tag.give}")
    private String zsTag;


    @Autowired
    private SplitOrderService splitOrderService;

    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;

    @Autowired
    private  DdbMqMessageRecordMapper ddbMqMessageRecordMapper;



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
        consumer.subscribe(consumerTopic, zsTag, new MessageOrderListener() {
            @Override
            public OrderAction consume(com.aliyun.openservices.ons.api.Message message, ConsumeOrderContext context) {
                try{
                    log.info("消费消息messageId" + message.getMsgID() + "message" + message.toString());
                    String messageBody = new String(message.getBody());
                    DdbConsumerHistory ddbConsumerHistory = ddbConsumerHistoryMapper.checkMessageId(message.getKey());
                    if(ddbConsumerHistory == null){
                        DdbOrder order = JSONObject.parseObject(messageBody, DdbOrder.class);
                        DdbGoodsRule goodRule = new DdbGoodsRule();
                        goodRule.setZid(order.getRuleId());
                        log.info("赠送订单处理开始flowId："+order.getFlowId());
                        splitOrderService.splitOrder(order,message);
                        log.info("赠送订单处理结束");
                    }else {
                        log.info("消息已处理过MessageId="+message.getMsgID());
                    }
                    return OrderAction.Success;
                }catch (Exception e ){
                    log.error("赠送订单处理异常",e);
                    return OrderAction.Suspend;
                }
            }
        });
        consumer.start();
    }

}
