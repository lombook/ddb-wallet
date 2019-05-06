package com.jinglitong.wallet.api.feign;


import com.jinglitong.wallet.api.feign.hystrix.AppWalletHystrixApi;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 释放
 */
@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-customer/ReleaseJob", fallback = AppWalletHystrixApi.class)
public interface FrozenReleaseFeignApi {


    @PostMapping("/releaseFeign")
    void release();

    @PostMapping("/checkFeign")
    void check();
}
