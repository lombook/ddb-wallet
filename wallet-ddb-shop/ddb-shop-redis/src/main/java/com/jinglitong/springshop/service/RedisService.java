package com.jinglitong.springshop.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisService
 * @Description redis服务接口
 * @Author zili.zong
 * @Date 2019/1/7 14:13
 * @Version 1.0
 **/
@Service
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> vo;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> objectValueOperations;


    public boolean hasKey(String key) {
    	objectValueOperations.increment("", 1);
        return stringRedisTemplate.hasKey(key);
    }

    public void setKey(String key, String value, Integer time, TimeUnit unit) {
        vo.set(key, value, time, unit);//多少时间过期
    }

    public String getValue(String key) {
        return vo.get(key);
    }

    public boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    public void setObject(String key, Object object) {
        objectValueOperations.set(key, object);
    }

    public void setObjectWithTime(String key, Object object, Integer time, TimeUnit unit) {
        objectValueOperations.set(key, object, time, unit);
    }

    public Object getObject(String key) {
        return objectValueOperations.get(key);
    }

    public boolean checkToken(String token){
        return hasKey(token);
    }
}
