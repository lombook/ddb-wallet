package com.jinglitong.wallet.checkserver.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.checkserver.service.AliCloudMailService;
import com.jinglitong.wallet.checkserver.service.DdbIntegralAccountService;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;


/**
 * 稽查核对定时任务
 *
 */
@Component
@EnableScheduling
public class CheckIntegralAccountTask implements SchedulingConfigurer {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${check.cron}")
	private String cron;
	
	@Value("${check.intervalTime}")
	private long interval;
	
	
	
	@Autowired
	DdbIntegralAccountService ddbIntegralAccountService;
	
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Runnable task0 = new Runnable() {
			@Override
			public void run() {
				logger.info("ddbIntegralAccountService satrt");
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//获得当前时间
				Date curTime = new Date();
				//时间间隔之前的时间
				Date beforeInterval = new Date(curTime.getTime() - interval*60*60*1000 - 60 * 1000);
				Date faterInterval = new Date(curTime.getTime() - 60*1000);
				//该段时间之内的稽核校验
				ddbIntegralAccountService.getCheck(df.format(beforeInterval), df.format(faterInterval));
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
