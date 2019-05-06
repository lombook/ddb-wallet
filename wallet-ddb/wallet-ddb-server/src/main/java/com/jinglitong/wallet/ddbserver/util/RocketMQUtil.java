package com.jinglitong.wallet.ddbserver.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;


@Slf4j
public class RocketMQUtil {

    /**
     * 简单的消息的发送
     * @param nameSrverAddress
     * @param producerGroup
     * @param message
     * @return
     */
    public static SendResult simpleProvider(String nameSrverAddress, String producerGroup, Message message){
        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(nameSrverAddress);
        SendResult sendResult = null;
        try{
            producer.start();
             sendResult = producer.send(message);
            if("SEND_OK".equals(sendResult.getSendStatus().toString())){
            }
            log.info("发送响应：MsgId:" + sendResult.getMsgId() + "，发送状态:" + sendResult.getSendStatus());
        }catch (Exception e){
            log.error("===MQ发送异常==="+e);
        }finally {
            producer.shutdown();
        }
        return sendResult;
    }


    /**
     * 定时消费发送
     * @param nameSrverAddress
     * @param producerGroup
     * @param message
     * @param timeLevel  时间层级 1s(1) 5s(2) 10s(3) 30s(4) 1m(5) 2m(6) 3m(7) 4m(8) 5m(9) 6m(10) 7m(11) 8m(12) 9m(13) 10m(14) 20m(15) 30m(16) 1h(17) 2h(18)
     * @return
     */
    public static SendResult scheduleProvider(String nameSrverAddress, String producerGroup, Message message,int timeLevel ){
        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(nameSrverAddress);
        SendResult sendResult = null;
        try{
            producer.start();
            message.setDelayTimeLevel(timeLevel);
            // Send the message
             sendResult = producer.send(message);
            if("SEND_OK".equals(sendResult.getSendStatus().toString())){
            }
            System.out.println("发送响应：MsgId:" + sendResult.getMsgId() + "，发送状态:" + sendResult.getSendStatus());
        }catch (Exception e){
            log.error("===MQ发送异常==="+e);
        }finally {
            producer.shutdown();
        }
        return sendResult;
    }


    /**
     * 有序消费发送
     * @param nameSrverAddress
     * @param producerGroup
     * @param message
     * @param queenNumber  队列编号
     * @return
     */
    public static SendResult orderProvider(String nameSrverAddress, String producerGroup, Message message,int queenNumber ){
        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(nameSrverAddress);
        SendResult sendResult = null;
        try{
            producer.start();
            // Send the message
             sendResult = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, queenNumber);
            System.out.println("发送响应：MsgId:" + sendResult.getMsgId() + "，发送状态:" + sendResult.getSendStatus());
        }catch (Exception e){
            log.error("===MQ发送异常==="+e);
        }finally {
            producer.shutdown();
        }
        return sendResult;
    }
}
