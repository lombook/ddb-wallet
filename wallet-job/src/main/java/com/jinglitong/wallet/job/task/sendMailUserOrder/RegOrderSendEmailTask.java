package com.jinglitong.wallet.job.task.sendMailUserOrder;

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

import com.jinglitong.wallet.job.service.RegOrderSendEmailService;

import com.jinglitong.wallet.job.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class RegOrderSendEmailTask implements SchedulingConfigurer{
	
	@Autowired
	private RegOrderSendEmailService RegOrderSendEmailService;
    /**
     * 任务开关
     */
    @Value("${statistics.cron}")
    private String cron;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Runnable task0 = new Runnable() {
			@Override
			public void run() {
				log.info(DateUtils.getDateTime()+"RegOrderSendEmailTask satrt");
				RegOrderSendEmailService.sendEamil();
				log.info(DateUtils.getDateTime()+"RegOrderSendEmailTask end");
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
