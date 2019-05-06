package com.jinglitong.wallet.sendserver.MQ;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.sendserver.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.sendserver.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;


/**
 * @author fyy
 * @create 2018-10-24-18:48}
 */
@Service
@Slf4j
public class SendConsumer {

    @Value("${ali.mq.send.group}")
    private String groupId;

    @Value("${ali.mq.topic}")
    private String consumerTopic;

    @Value("${ali.mq.send.tag}")
    private String sendTag;

    @Value("${ali.mq.accessKey}")
    private String accessKey;

    @Value("${ali.mq.secretKey}")
    private String secretKey;

    @Value("${ali.mq.server.addr}")
    private String serverAddr;


    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;

    @Autowired
    private SendService sendService;


    @PostConstruct//@PostContruct是spring框架的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。
    public void defaultMQPushConsumer() {
        Properties properties = new Properties();
        // 您在控制台创建的 Consumer ID
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
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
        OrderConsumer consumer = ONSFactory.createOrderedConsumer(properties);
        consumer.subscribe(consumerTopic, sendTag, new MessageOrderListener() {
            @Override
            public OrderAction consume(com.aliyun.openservices.ons.api.Message message, ConsumeOrderContext context) {
                try{

                    log.info("消费消息messageId" + message.getMsgID() + "message" + message.toString());
                    DdbConsumerHistory ddbConsumerHistory = ddbConsumerHistoryMapper.selectByMessId(message.getKey());
                    if(ddbConsumerHistory == null){

                        sendService.sendMessage(message);

                    }else {
                        log.info("消息已处理过MessageId="+message.getMsgID());
                    }
                    return OrderAction.Success;
                }catch (Exception e ){
                    return OrderAction.Suspend;
                }
            }
        });
        consumer.start();
    }
}

