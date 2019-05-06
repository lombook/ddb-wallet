package com.jinglitong.wallet.ddbapi.feign.hystrix;

import com.jinglitong.wallet.ddbapi.feign.SynchronizeToGameFeignApi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SynchronizeToGameHystrixApi implements SynchronizeToGameFeignApi{

    
    @Override
    public Map SynchronizeInviteToGame( Map map) {
    	Map result = new HashMap();
    	result.put("code", "ERROR");
    	result.put("message", "失败");
    	return result ;
    };
}
