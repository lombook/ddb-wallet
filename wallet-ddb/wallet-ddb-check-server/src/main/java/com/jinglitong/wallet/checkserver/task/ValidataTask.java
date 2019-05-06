/*package com.jinglitong.wallet.checkserver.task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.jinglitong.wallet.checkserver.service.ValidataService;

import lombok.extern.slf4j.Slf4j;

*//**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ValidataTask.java
 * 作        者: yxl 2018年10月31日
 * 创建时间: 2018年10月31日
 * 功能说明:每两小时检查前三小时的数据
 *//*
@Component
@Slf4j
@EnableScheduling
public class ValidataTask  implements SchedulingConfigurer {

	@Autowired
	private ValidataService validataService;
    *//**
     * 任务开关
     *//*
    @Value("${validataJob.cron}")
    private String cron;
    
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Runnable task0 = new Runnable() {
			@Override
			public void run() {
				log.info("validataService satrt");
				validataService.validata();
			}
		};
		Trigger trigger0 = new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// 任务触发.
				CronTrigger trigger = new CronTrigger(cron);
				Date nextExec = trigger.nextExecutionTime(triggerContext);
				return nextExec;
			}
		};
		taskRegistrar.addTriggerTask(task0, trigger0);
	}
	
}
*/