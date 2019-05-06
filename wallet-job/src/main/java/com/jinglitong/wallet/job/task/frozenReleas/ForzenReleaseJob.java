package com.jinglitong.wallet.job.task.frozenReleas;

import com.jinglitong.wallet.job.service.MiddlePayService;
import com.jinglitong.wallet.job.service.ReleaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ForzenReleaseJob  implements SchedulingConfigurer {


	@Autowired
	private ReleaseService releaseService;

	@Autowired
	private MiddlePayService middlePayService;


    @Value("${fr.open}")
    private int frOpen ;

	@Value("${fc.open}")
	private int fcOpen ;

	@Value("${fr.cron}")
	private String frCrom;

	@Value("${fc.cron}")
	private String fcCron;

	private static   Boolean  releaseFlag = true;

	private static   Boolean  checkFlag = true;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(
				()-> {
					if(frOpen == 1){
						if(releaseFlag ){
							try {
								releaseFlag = false;
								long start = System.currentTimeMillis();
								log.debug("FrozenRelease release tast begin time is "+start);
								middlePayService.createMiddelPayRecord();
								releaseService.releaseAmount();
								long end = System.currentTimeMillis();
								log.debug("FrozenRelease release tast end time is "+end);
								log.info("FrozenRelease release tast consume time is "+(end - start) + "Mill");
							}catch (Exception e){
								log.error(e.toString());
							}finally {
								releaseFlag = true;
							}
						}

					}
				},
				triggerContext ->{ return new CronTrigger(frCrom).nextExecutionTime(triggerContext);});

		taskRegistrar.addTriggerTask(
				()-> {
					if(fcOpen == 1){
						if(checkFlag){
							try {
								checkFlag = false;
								long start = System.currentTimeMillis();
								log.debug("FrozenRelease check tast start time is "+start);
								releaseService.checkRelease();
								long end = System.currentTimeMillis();
								log.debug("FrozenRelease check tast end time is "+end);
								log.info("FrozenRelease check tast consume time is "+(end - start) + "Mill");
							}catch (Exception e){
								log.error(e.toString());
							}finally {
								checkFlag = true;
							}
						}

					}
				},
				triggerContext ->{ return new CronTrigger(fcCron).nextExecutionTime(triggerContext);});
	}

}
