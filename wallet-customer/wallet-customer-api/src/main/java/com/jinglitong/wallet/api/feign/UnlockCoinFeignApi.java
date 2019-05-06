package com.jinglitong.wallet.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.jinglitong.wallet.api.feign.hystrix.UnlcokCoinHystrixApi;

import feign.Headers;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: UnlockCoinFeignApi.java
 * 作        者: yxl 2018年7月26日
 * 创建时间: 2018年7月26日
 * 功能说明:锁仓接口
 */
@Headers("Content-Type:application/json")
@FeignClient(name="wallet-customer/unlockCoinJob",fallback=UnlcokCoinHystrixApi.class)
public interface UnlockCoinFeignApi {
	
	/**
	 * 
	 * 功能说明:执行解仓转账
	 */
	@PostMapping("excueteTaskFeign")
	void excueteTask();
	
	/**
	 * 
	 * 功能说明:修改轉賬后的狀態
	 */
	@PostMapping("updateStatusFeign")
	void updateStatus();
}
