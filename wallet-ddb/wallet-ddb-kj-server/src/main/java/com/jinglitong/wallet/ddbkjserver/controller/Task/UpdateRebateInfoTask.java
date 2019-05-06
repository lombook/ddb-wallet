package com.jinglitong.wallet.ddbkjserver.controller.Task;


import com.jinglitong.wallet.api.feign.LockCoinRecordFeignApi;
import com.jinglitong.wallet.ddbapi.model.DdbGrowSeed;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbGrowSeedMapper;
import com.jinglitong.wallet.ddbkjserver.service.ValidationRevenueSharingService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: GrowSeedJob.java
 * 作        者: wyw
 * 创建时间: 2018年10月23日
 * 功能说明:扫描育苗表并保存到队列定时任务
 */
@Component
@Slf4j
public class UpdateRebateInfoTask {

    /**
     * 任务开关
     */
    @Value("${updateRebateInfo.open}")
    private int updateRebateInfo;

    @Autowired
    private ValidationRevenueSharingService validationRevenueSharingService;
    
    /**
     * 确定任务状态
     */
    @Scheduled(cron = "${updateRebateInfo.cron}")
    public void confirmPayStatus(){
        if(updateRebateInfo!=1){
            return;
        }
        try{
           //更新内存中分红比例信息
        	validationRevenueSharingService.checkAndUpdateStart();
        }catch (Exception e){
        	//打印错误信息
            log.error("error: "+e.getMessage());
            e.printStackTrace();
        }
    }
}
