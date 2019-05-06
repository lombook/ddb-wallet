package com.jinglitong.wallet.ddbserver.job;

import com.jinglitong.wallet.ddbserver.service.MQProductAgainService;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ValidataTask.java
 * 作        者: yxl 2018年11月14日
 * 创建时间: 2018年11月14日
 * 功能说明:
 */
@Component
@Slf4j
@EnableScheduling
public class MQProductAgainTask implements SchedulingConfigurer {

	@Autowired
	private MQProductAgainService MQProductAgainService;
    /**
     * 任务开关
     */
    @Value("${product.cron}")
    private String cron;
    
    @Value("${product.open}")
    private int open;
    
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		if(open == 1) {
			Runnable task0 = new Runnable() {
				@Override
				public void run() {
					log.info(DateUtils.getDateTime()+"MQProductAgainService satrt");
					MQProductAgainService.start();
					log.info(DateUtils.getDateTime()+"MQProductAgainService end");
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
		}else {
			log.info("MQProductAgainTask closed");
		}
	}
	
}
