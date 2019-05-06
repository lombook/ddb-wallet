package com.jinglitong.springshop.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.jinglitong.springshop.mq.MqProductService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright (c) 2019, 井立通
 * All rights reserved.
 * 文件名称: MqProductJob.java
 * 作        者: yxl 2019年1月25日
 * 创建时间: 2019年1月25日
 * 功能说明:重试向mq发送消息
 */
@Component
@Slf4j
public class MqProductJob implements SchedulingConfigurer {


    @Value("${mq.job.open}")
    private int mqOpen;

    @Value("${mq.job.cron}")
    private String cron;

    @Autowired
    private MqProductService mqProductService;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                ()-> {
                    if(mqOpen == 1){
                        long start = System.currentTimeMillis();
                        log.debug(" MQ task start time is " + start);
                        mqProductService.start();
                        long end = System.currentTimeMillis();
                        log.debug(" MQ task end time is "+end);
                        log.info(" MQ task consume time is "+(end- start) + "Mill");
                    }
                },
                triggerContext ->{ return new CronTrigger(cron).nextExecutionTime(triggerContext);});
    }
}
