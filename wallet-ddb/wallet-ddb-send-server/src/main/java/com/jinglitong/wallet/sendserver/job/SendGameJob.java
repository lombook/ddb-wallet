package com.jinglitong.wallet.sendserver.job;

import com.jinglitong.wallet.sendserver.service.SendGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendGameJob implements SchedulingConfigurer {

	@Autowired
	private SendGameService sendGameService;

    @Value("${send.open}")
    private int sendOpen = 1;

    @Value("${send.cron}")
	private  String sendCron;

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		scheduledTaskRegistrar.addTriggerTask(
				()-> {
					if(sendOpen == 1){
						long start = System.currentTimeMillis();
						log.debug("SendGame task start time is "+start);
						sendGameService.send();
						long end = System.currentTimeMillis();
						log.debug("SendGame task end time is "+end);
						log.info("SendGame task consume time is "+ (end - start)+"Mill.");
					}
				},
				triggerContext ->{ return new CronTrigger(sendCron).nextExecutionTime(triggerContext);});
	}
}
