package com.jinglitong.wallet.api.feign;

import com.jinglitong.wallet.api.feign.hystrix.AppWalletHystrixApi;
import com.jinglitong.wallet.common.utils.R;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-customer", fallback = AppWalletHystrixApi.class)
public interface AppWalletFeignApi {
    @PostMapping("/appWalletFeign/checkAppIdExistFeign")
    Long getAppWalletIdByIden(@RequestParam("appIden") String appIden);

}
