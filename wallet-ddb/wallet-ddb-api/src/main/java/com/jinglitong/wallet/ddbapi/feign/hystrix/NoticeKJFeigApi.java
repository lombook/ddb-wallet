package com.jinglitong.wallet.ddbapi.feign.hystrix;

import com.jinglitong.wallet.ddbapi.feign.NoticeKJFeignApi;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NoticeKJFeigApi implements NoticeKJFeignApi {

    
    @Override
    public Map order(KJNotice notice) {
    	Map result = new HashMap();
    	result.put("code", "ERROR");
    	result.put("message", "失败");
    	return result ;
    };
}
