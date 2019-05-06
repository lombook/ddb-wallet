package com.jinglitong.wallet.api.feign;


import com.jinglitong.wallet.api.feign.hystrix.AppWalletHystrixApi;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 锁仓检查是否到账
 */
@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-customer/lockCoinRecordJob", fallback = AppWalletHystrixApi.class)
public interface LockCoinRecordFeignApi {

    /**
     * 确认状态 是否到账
     */
    @PostMapping("confirmPayStatusFeign")
    void confirmPayStatus();
}
