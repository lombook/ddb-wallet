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
import com.jinglitong.wallet.job.service.CheckBaoFenExchangeMEIService;
import com.jinglitong.wallet.job.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableScheduling
public class CheckBaoFenExchangeMEITask implements SchedulingConfigurer {
	
	private static int state = 0;
	/**
	 * 任务开关
	 */
	@Value("${CheckBaoFenExchangeMEI.open}")
	private int checkbaoFenExchangeMEIOpen;

	/**
	 * 任务执行周期
	 */
	@Value("${CheckBaoFenExchangeMEI.cron}")
	private String cron;
	
	@Autowired
    private	CheckBaoFenExchangeMEIService  checkBaoFenExchangeMEIService;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
	// TODO Auto-generated method stub
			if(state == 1) {// 如果一个定时任务 在执行   ，那么 下一个定时  不能执行
				return;
			}

			Runnable task0 = new Runnable() {
				@Override
				public void run() {
					log.info(DateUtils.getDateTime() + "CheckBaoFenExchangeMEITask satrt,检查宝分上链是否转账成功任务开始...");
					if(checkbaoFenExchangeMEIOpen != 1) {
						return;
					}
					// 定时任务开始执行  等于1
					state = 1;
					checkBaoFenExchangeMEIService.start();
					log.info(DateUtils.getDateTime() + "CheckBaoFenExchangeMEITask end,检查宝分上链是否转账成功任务结束...");
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
