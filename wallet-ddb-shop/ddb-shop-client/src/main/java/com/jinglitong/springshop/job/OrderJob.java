package com.jinglitong.springshop.job;

import com.jinglitong.springshop.servcie.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author fyy
 * @create 2019-01-23-17:19}
 */

@Component
@Slf4j
public class OrderJob implements SchedulingConfigurer {

    @Value("${order.expire.open}")
    private int orderExpireOpen = 0;

    @Value("${order.expire.cron}")
    private String orderExpirecron ;

    @Value("${order.complitecorn.open}")
    private int orderCompliteOpen = 0;

     @Value("${order.complitecorn.cron}")
    private String orderComplitecron ;

    @Autowired
    private OrderService orderService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                ()-> {
                    if(orderExpireOpen == 1){
                        long start = System.currentTimeMillis();
                        log.debug("orderExpire task start time is " + start);
                        orderService.orderExpire();
                        long end = System.currentTimeMillis();
                        log.debug("orderExpire task end time is "+end);
                        log.info("orderExpire task consume time is "+(end- start) + "Mill");
                    }
                },
                triggerContext ->{ return new CronTrigger(orderExpirecron).nextExecutionTime(triggerContext);});

        taskRegistrar.addTriggerTask(
                ()-> {
                    if(orderCompliteOpen == 1){
                        long start = System.currentTimeMillis();
                        log.info("orderComplite task start time is " + start);
                        orderService.orderComplate();
                        long end = System.currentTimeMillis();
                        log.info("orderComplite task end time is "+end);
                        log.info("orderComplite task consume time is "+(end- start) + "Mill");
                    }
                },
                triggerContext ->{ return new CronTrigger(orderComplitecron).nextExecutionTime(triggerContext);});
    }
}
