package com.jinglitong.wallet.server.controller.job;


import com.jinglitong.wallet.api.feign.FrozenReleaseFeignApi;
import com.jinglitong.wallet.server.service.ReleaseService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("ReleaseJob")
@RestController
@Slf4j
public class ReleaseJob  implements FrozenReleaseFeignApi {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/releaseFeign")
    public void release() {

    }

    @PostMapping("/checkFeign")
    public void check() {

    }
}
