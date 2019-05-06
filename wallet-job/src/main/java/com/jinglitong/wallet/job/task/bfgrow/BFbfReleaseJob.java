package com.jinglitong.wallet.job.task.bfgrow;

import com.jinglitong.wallet.job.service.BFFrozenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BFbfReleaseJob implements SchedulingConfigurer {

	@Autowired
	private BFFrozenService bfFrozenService;

    @Value("${bf.open}")
    private int bFOpen ;

    @Value("${bf.cron}")
    private String cron;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(
				()-> {
						if(bFOpen == 1){
							long start = System.currentTimeMillis();
							log.debug("BFGrow task start time is " + start);
							bfFrozenService.releaseBf();
							long end = System.currentTimeMillis();
							log.debug("BFGrow task end time is "+end);
							log.info("BFGrow task consume time is "+(end- start) + "Mill");
						}
					},
				triggerContext ->{ return new CronTrigger(cron).nextExecutionTime(triggerContext);});
	}
}
