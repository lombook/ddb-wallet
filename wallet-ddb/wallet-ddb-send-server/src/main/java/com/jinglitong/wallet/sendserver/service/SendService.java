package com.jinglitong.wallet.sendserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbapi.model.DdbSendRecord;
import com.jinglitong.wallet.sendserver.mapper.DdbConsumerHistoryMapper;
import com.jinglitong.wallet.sendserver.mapper.DdbSendRecordMapper;
import com.jinglitong.wallet.sendserver.util.DateUtils;
import com.jinglitong.wallet.sendserver.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Slf4j
@Service
@Transactional
public class SendService {

    @Value("${send.retry.time}")
    private String  retryTime;

    @Autowired
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;

    @Autowired
    private DdbSendRecordMapper ddbSendRecordMapper;


    public void sendMessage(com.aliyun.openservices.ons.api.Message message){
        String date = DateUtils.getDateTime();
        String messageBody = new String(message.getBody());
        //解析messageBody
        String parse = JSONObject.parse(messageBody).toString();
        Map<String,String> data = JSON.parseObject(parse, Map.class);
        log.info(data.toString());
        //取出地址address
        String address = data.get("address");
        String flowId = data.get("flowId");
        log.info("游戏通知处理开始flowId："+flowId);
        //取出body
        String body = data.get("body");
        //调用httpUtil发送
        String result = null;
        Map<String,Object> resultMap = null;
        try{
            result  = HttpUtil.sendPostJson(address, body);
            resultMap = (Map<String,Object>)JSON.parse(result);
        }catch (Exception ree ){
            log.info("请求失败服务未启动"+ree);
        }
        //验证结果
        log.info("请求结果"+resultMap);
        if(resultMap != null){
            int code = (int)resultMap.get("code");
            if(code == 0){
                //成功
                log.info("请求成功");
                DdbSendRecord ddbSendRecord = new DdbSendRecord(flowId, address, 1,DateUtils.getDateTime(),0, date, body);
                ddbSendRecordMapper.insert(ddbSendRecord);
            }else {
                log.info("请求失败");
                String reTime = DateUtils.formatDateTime(DateUtils.addMinutes(new Date(), Integer.valueOf(retryTime)));
                DdbSendRecord ddbSendRecord = new DdbSendRecord(flowId, address, 2,reTime,0, date, body);
                ddbSendRecordMapper.insert(ddbSendRecord);
            }
        }else {
            String reTime = DateUtils.formatDateTime(DateUtils.addMinutes(new Date(), Integer.valueOf(retryTime)));
            DdbSendRecord ddbSendRecord = new DdbSendRecord(flowId, address, 2,reTime,0, date, body);
            ddbSendRecordMapper.insert(ddbSendRecord);
        }


        log.info("赠送订单处理结束");

        DdbConsumerHistory ddbConsumerInsert = new DdbConsumerHistory();
        ddbConsumerInsert.setMsgId(message.getMsgID());
        ddbConsumerInsert.setFlowId(message.getKey());
        ddbConsumerInsert.setCreateTime(DateUtils.getDateTime());
        ddbConsumerHistoryMapper.insert(ddbConsumerInsert);
    }

}
