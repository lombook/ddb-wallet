package com.jinglitong.wallet.ddbapi.feign;

import com.jinglitong.wallet.ddbapi.feign.hystrix.SynchronizeToGameHystrixApi;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Headers("Content-Type:application/json") 
@FeignClient(name = "wallet-ddb-kj/kjnotice", fallback = SynchronizeToGameHystrixApi.class)
public interface NoticeKJFeignApi {
    @PostMapping("/order.json")
    Map order(@RequestBody KJNotice notice);

}
