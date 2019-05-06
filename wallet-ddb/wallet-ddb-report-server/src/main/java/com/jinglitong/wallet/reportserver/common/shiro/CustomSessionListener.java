package com.jinglitong.wallet.reportserver.common.shiro;



import com.jinglitong.wallet.reportserver.conf.RedisConfiguration;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;


@Configuration
public class CustomSessionListener implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisConfiguration redisConfiguration;


    @Override
    public void onStart(Session session) {
        logger.debug("onStart:{}", session.getId());
    }

    @Override
    public void onStop(Session session) {
        logger.debug("onStop:{}", session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        logger.debug("onExpiration:{}", session.getId());
        redisTemplate.delete(redisConfiguration.getSessionPrefix() + session.getId().toString());
    }

}

