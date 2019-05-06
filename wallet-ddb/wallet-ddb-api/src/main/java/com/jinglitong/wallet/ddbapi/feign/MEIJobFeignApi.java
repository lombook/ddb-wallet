package com.jinglitong.wallet.ddbapi.feign;


import com.jinglitong.wallet.api.feign.hystrix.AppWalletHystrixApi;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * MEI释放
 */
@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-ddb/MEISynJob", fallback = AppWalletHystrixApi.class)
public interface MEIJobFeignApi {
    @PostMapping("/MEIreleaseFeign")
    void MEIrelease();

    @PostMapping("/MEIcheckFeign")
    void MEIcheck();

    @PostMapping("/MEIreMoveRequireFeign")
    void MEIreMoveRequire();
}
