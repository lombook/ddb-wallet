package com.jinglitong.wallet.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.jinglitong.wallet.api.feign.hystrix.NotifySellerHystrixApi;

import feign.Headers;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: NotifySellerFeignApi.java
 * 作        者: yxl 2018年8月9日
 * 创建时间: 2018年8月9日
 * 功能说明:支付通知订单状态
 */
@Headers("Content-Type:application/json")
@FeignClient(name="wallet-customer/notifySellerJob",fallback=NotifySellerHystrixApi.class)
public interface NotifySellerFeignApi {
	
	@PostMapping("excueteTaskFeign")
	void excueteTask();
}
