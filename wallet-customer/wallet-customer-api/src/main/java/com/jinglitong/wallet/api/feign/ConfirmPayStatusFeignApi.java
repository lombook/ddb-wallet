package com.jinglitong.wallet.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.jinglitong.wallet.api.feign.hystrix.ConfirmPayStatusHystrixApi;

import feign.Headers;

@Headers("Content-Type:application/json")
@FeignClient(name = "wallet-customer/PayStatusJob", fallback = ConfirmPayStatusHystrixApi.class)
public interface ConfirmPayStatusFeignApi {

	/**
	 * 
	 * 功能说明:查询支付订单是否到账
	 */
	@PostMapping("excueteTaskFeign")
	void excueteTask();
}
