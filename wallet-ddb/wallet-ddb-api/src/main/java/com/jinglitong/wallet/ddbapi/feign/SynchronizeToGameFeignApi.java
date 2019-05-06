package com.jinglitong.wallet.ddbapi.feign;

import com.jinglitong.wallet.api.feign.hystrix.AppWalletHystrixApi;
import com.jinglitong.wallet.common.utils.R;
import com.jinglitong.wallet.ddbapi.feign.hystrix.SynchronizeToGameHystrixApi;

import feign.Headers;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Headers("Content-Type:application/json") 
@FeignClient(name = "wallet-ddb/SynchronizeToGameFeign", fallback = SynchronizeToGameHystrixApi.class)
public interface SynchronizeToGameFeignApi {
    @PostMapping("/SynchronizeInviteToGameFeign")
    Map SynchronizeInviteToGame(@RequestBody Map map);

}
