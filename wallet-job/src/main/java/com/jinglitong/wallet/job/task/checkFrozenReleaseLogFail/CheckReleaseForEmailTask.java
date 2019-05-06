package com.jinglitong.wallet.job.task.checkFrozenReleaseLogFail;

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

import com.jinglitong.wallet.job.service.CheckReleaseForEmailService;
import com.jinglitong.wallet.job.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class CheckReleaseForEmailTask implements SchedulingConfigurer {
	
	
	/**
	 * 任务开关 
	 */
	@Value("${checkRealseJob.open}")
	private int checkRealseJobOpen ;
	
	/**
     * 任务执行周期
     */
    @Value("${checkRealseJob.cron}")
    private String cron;
    @Autowired
    private CheckReleaseForEmailService checkReleaseForEmailService;
    
    private static int state = 0;
    
	/**
	 * 检查冻结释放失败表，做转账处理，转账失败三次发邮件
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		if(state == 1) {// 如果一个定时任务 在执行   ，那么 下一个定时  不能执行
			return;
		}
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				log.info(DateUtils.getDateTime()+"CheckReleaseForEmailService satrt");
				if(checkRealseJobOpen != 1) {
					return;
				}
				// 定时任务开始执行  等于1
				state = 1;
				checkReleaseForEmailService.start();
				log.info(DateUtils.getDateTime()+"CheckReleaseForEmailService end");
			}
		};
		Trigger trigger = new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				// 任务触发.
				CronTrigger trigger = new CronTrigger(cron);
				Date nextExec = trigger.nextExecutionTime(triggerContext);
				return nextExec;
			}
		};
		taskRegistrar.addTriggerTask(runnable, trigger);
		// 定时任务 执行完等于 0
		state = 0;
	}

	
}
