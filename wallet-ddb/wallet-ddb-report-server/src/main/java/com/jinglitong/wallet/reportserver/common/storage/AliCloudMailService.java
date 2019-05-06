package com.jinglitong.wallet.reportserver.common.storage;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by fan on 2018/5/25.
 */
//@Service
public class AliCloudMailService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${aliyun.mail.accessKey}")
    private String accessKeyId;
    @Value("${aliyun.mail.secret}")
    private String secret;
    @Value("${aliyun.mail.regionId}")
    private String orgionId;
    @Value("${aliyun.mail.accountName}")
    private String accountName;

    private final static String LONG_STR = "1234567890";

    public Boolean sendMail(String toAddress,String subject, String body ) {
        Boolean flag =  true;
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的"cn-hangzhou"替换为"ap-southeast-1"、或"ap-southeast-2"。
        IClientProfile profile = DefaultProfile.getProfile(orgionId, accessKeyId, secret);
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        if (!orgionId.equals("cn-hangzhou")) {
            try {
                DefaultProfile.addEndpoint("dm.ap-southeast-1.aliyuncs.com", "ap-southeast-1", "Dm", "dm.ap-southeast-1.aliyuncs.com");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            if (!orgionId.equals("cn-hangzhou"))
                request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            request.setActionName("SingleSendMail");
            request.setAccountName(accountName);
            // request.setFromAlias("");
            request.setAddressType(1);
            //request.setTagName("控制台创建的标签");
            request.setReplyToAddress(true);
            request.setToAddress(toAddress);
            request.setSubject(subject);
            request.setHtmlBody(body);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        logger.debug("邮件发送：toAddress(地址):"+toAddress+"。邮件内容:"+body);
        return flag;
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
}
