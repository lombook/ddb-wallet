package com.jinglitong.wallet.ddbserver.util;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinglitong.wallet.api.model.logic.LSmsVO;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.ddbserver.service.AppWalletService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jinglitong.wallet.ddbserver.common.SMSEnum.FORGET;
import static com.jinglitong.wallet.ddbserver.common.SMSEnum.MODPASS;
import static com.jinglitong.wallet.ddbserver.common.SMSEnum.REGIST;
import static com.jinglitong.wallet.ddbserver.common.SMSEnum.BINDDEVICE;
import static com.jinglitong.wallet.ddbserver.common.SMSEnum.EXPORT;


@Component
public class SmsUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AppWalletService appWalletService;

    private final String CLASS_NAME = "SmsUtil";

    private final static String resSuc = "success";

    private final static String LONG_STR = "1234567890";



    @Value("${sms.appid}")
    private String appid;
    @Value("${sms.appkey}")
    private String appkey;

    private String regProject;
    private String forgetProject;

    @Value("${sms.sendurl}")
    private String sendUrl;

    public boolean sendSms(LSmsVO sms){
        PropertieVO propertieVO = new PropertieVO();
        propertieVO.setAppId(sms.getAppId());
        List<Map<String, Object>> propertiesMap = appWalletService.getPropertiesMap(propertieVO);
        regProject = propertiesMap.get(0).get("smsRegCode").toString();
        forgetProject = propertiesMap.get(0).get("smsValidationCode").toString();

        Long startTime = System.currentTimeMillis();
        //获取对话
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //JSON contentType
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //我们发起 HTTP 请求还是最好加上"Connection","close" ，有利于程序的健壮性
        headers.set("Connection","close");
        //设置参数
        Map<String, Object> hashMap = new LinkedHashMap<String, Object>();
        Map<String, String> varMap = new LinkedHashMap<String, String>();
        varMap.put("code",sms.getCode());
        hashMap.put("appid", appid);
        hashMap.put("to", sms.getPhone());
        if (FORGET.toString().equals(sms.getType())){
            hashMap.put("project",forgetProject);
        }
        if (REGIST.toString().equals(sms.getType())){
            hashMap.put("project",regProject);
        }
        if (MODPASS.toString().equals(sms.getType())){
            hashMap.put("project",forgetProject);
        }
        if (BINDDEVICE.toString().equals(sms.getType())){
            hashMap.put("project",forgetProject);
        }
        if (EXPORT.toString().equals(sms.getType())){
            hashMap.put("project",forgetProject);
        }
        hashMap.put("vars",varMap);
        hashMap.put("signature",appkey);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String, Object>>(hashMap, headers);

        logger.debug(CLASS_NAME + "will send phone :"+sms.getPhone() + ",maps + "+ JSON.toJSON(sms));

        ResponseEntity<String> resp = restTemplate.exchange(sendUrl, HttpMethod.POST,requestEntity, String.class);
        String resTex = resp.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> resMap = null;
        try {
            resMap = mapper.readValue(resTex,Map.class);
        } catch (IOException e) {
            logger.error("send message error :" + e );
            return false;
        }
        Long endTime = System.currentTimeMillis();
        logger.debug(CLASS_NAME + "receive phone :"+sms.getPhone() + ",result:" +JSON.toJSON(resMap) +",cust "+(startTime-endTime)+" ms");

        if(resMap.containsKey("status") && resMap.get("status").equals(resSuc)) {
            return true;
        }
        return false;

    }

    public static String createCode(int length){
        String retStr = "";
        int len = LONG_STR.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = LONG_STR.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += LONG_STR.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

    public static void main(String[] args){
        for (int i=0;i<10;i++)
        System.out.println(SmsUtil.createCode(4));
    }

}
