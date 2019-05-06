package com.jinglitong.wallet.ddbkjserver.controller.Task;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.jinglitong.wallet.ddbkjserver.service.ShareholdersHierarchyValidationService;

/**
 * 用户层级结构校验定时任务
 * @author panzixin
 *
 */
/*@Component
@EnableScheduling*/
public class ShareholdersHierarchyValidationTask implements SchedulingConfigurer{

	@Autowired
	public static String cron = "0 0/15 * * * ?";
	@Autowired
	ShareholdersHierarchyValidationService hareholdersHierarchyValidationService;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Runnable task0 = new Runnable() {
			@Override
			public void run() {
				// 任务逻辑代码部分------------------------
				hareholdersHierarchyValidationService.startCheck();
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
