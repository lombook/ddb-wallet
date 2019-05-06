package com.jinglitong.wallet.sendserver.conf;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public class TaskSchedulerConf implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //创建一个线程池调度器
         ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 设置线程池容量
        scheduler.setPoolSize(50);
        // 线程名前缀
        scheduler.setThreadNamePrefix("task-send");
        // 等待时常
        scheduler.setAwaitTerminationSeconds(60);
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 设置当任务被取消的同时从当前调度器移除的策略
        scheduler.setRemoveOnCancelPolicy(true);
        // 设置任务注册器的调度器
        scheduledTaskRegistrar.setTaskScheduler(scheduler);

    }
}
