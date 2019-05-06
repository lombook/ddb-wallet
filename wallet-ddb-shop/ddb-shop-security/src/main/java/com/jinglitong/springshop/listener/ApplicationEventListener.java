package com.jinglitong.springshop.listener;

import com.jinglitong.springshop.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName ApplicationEventListener
 * @Description TODO
 * @Author zili.zong
 * @Date 2018/11/19 15:34
 * @Version 1.0
 **/
@Component
public class ApplicationEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private RedisService redisService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        redisService.setKey("admin","shop!@jlt^=2019.com",1, TimeUnit.DAYS);
    }
}
