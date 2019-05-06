package com.jinglitong.wallet.sendserver.service;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.ddbapi.model.DdbSendRecord;
import com.jinglitong.wallet.sendserver.mapper.DdbSendRecordMapper;
import com.jinglitong.wallet.sendserver.util.DateUtils;
import com.jinglitong.wallet.sendserver.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class SendGameService {

    @Autowired
    private DdbSendRecordMapper ddbSendRecordMapper;

    @Value("${send.retry.time}")
    private String retryTime;
    
    public void send(){
        List<DdbSendRecord> ddbSendRecordList = ddbSendRecordMapper.selectByExcuTime();
        for (DdbSendRecord ddbSend: ddbSendRecordList) {

            String address = ddbSend.getAddress();
            String body = ddbSend.getBody();
            //调用httpUtil发送
            String result = HttpUtil.sendPostJson(address, body);
            //验证结果
            Map<String,Object> resultMap = null;
            try{
                resultMap = (Map<String,Object>)JSON.parse(result);
            }catch (Exception e){
                log.info("请求结果"+result);
                resultMap = null;
            }
                if(resultMap != null){
                    int code = (int)resultMap.get("code");
                    if(code == 0){
                        //成功
                        log.info("请求成功");
                        DdbSendRecord ddbSendRecord = new DdbSendRecord();
                        ddbSendRecord.setId(ddbSend.getId());
                        ddbSendRecord.setType(1);
                        ddbSendRecordMapper.updateByPrimaryKeySelective(ddbSendRecord);
                    }else {
                        Integer retryTimes = ddbSend.getRetryTimes();
                        if(retryTimes == null){
                            retryTimes = 1;
                        }
                        String reTime = DateUtils.formatDateTime(DateUtils.addMinutes(new Date(), Integer.valueOf(retryTime) * retryTimes++ ));
                        log.info("请求失败");
                        DdbSendRecord ddbSendRecord = new DdbSendRecord();
                        ddbSendRecord.setId(ddbSend.getId());
                        ddbSendRecord.setRetryExcuseTime(reTime);
                        ddbSendRecord.setRetryTimes(retryTimes);
                        ddbSendRecordMapper.updateByPrimaryKeySelective(ddbSendRecord);
                    }
                }else {
                    Integer retryTimes = ddbSend.getRetryTimes();
                    if(retryTimes == null){
                        retryTimes = 1;
                    }else {
                        retryTimes++;
                    }
                    String reTime = DateUtils.formatDateTime(DateUtils.addMinutes(new Date(), Integer.valueOf(retryTime) * retryTimes ));
                    log.info("访问失败");
                    DdbSendRecord ddbSendRecord = new DdbSendRecord();
                    ddbSendRecord.setId(ddbSend.getId());
                    ddbSendRecord.setRetryExcuseTime(reTime);
                    ddbSendRecord.setRetryTimes(retryTimes);
                    ddbSendRecordMapper.updateByPrimaryKeySelective(ddbSendRecord);
                }
        }
   }

}
