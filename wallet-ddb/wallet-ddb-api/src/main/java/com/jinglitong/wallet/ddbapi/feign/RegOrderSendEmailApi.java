package com.jinglitong.wallet.ddbapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.jinglitong.wallet.ddbapi.feign.hystrix.RegOrderSendEmailJobHystrixApi;

import feign.Headers;

@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-ddb/regOrderSendEmail", fallback = RegOrderSendEmailJobHystrixApi.class)
public interface RegOrderSendEmailApi {
	
	@PostMapping("/sendEamil")
	void sendEamil();

}
