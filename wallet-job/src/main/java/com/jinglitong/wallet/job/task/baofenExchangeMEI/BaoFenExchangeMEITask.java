package com.jinglitong.wallet.job.task.baofenExchangeMEI;

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

import com.jinglitong.wallet.job.service.BaoFenExchangeMEIService;
import com.jinglitong.wallet.job.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class BaoFenExchangeMEITask implements SchedulingConfigurer {
	/**
	 * 任务开关
	 */
	@Value("${BaoFenExchangeMEI.open}")
	private int baoFenExchangeMEIOpen;

	/**
	 * 任务执行周期
	 */
	@Value("${BaoFenExchangeMEI.cron}")
	private String cron;
	
	@Autowired
    private	BaoFenExchangeMEIService  baoFenExchangeMEIService;
	
	private static int state = 0;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		if(state == 1) {// 如果一个定时任务 在执行   ，那么 下一个定时  不能执行
			return;
		}

		Runnable task0 = new Runnable() {
			@Override
			public void run() {
				log.info(DateUtils.getDateTime() + "BaoFenExchangeMEITask satrt,宝分上链转账开始...");
				if(baoFenExchangeMEIOpen != 1) {
					return;
				}
				// 定时任务开始执行  等于1
				state = 1;
				 baoFenExchangeMEIService.start();
				log.info(DateUtils.getDateTime() + "BaoFenExchangeMEITask end,宝分上链转账结束...");
				// 定时任务 执行完等于 0
				state = 0;
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
