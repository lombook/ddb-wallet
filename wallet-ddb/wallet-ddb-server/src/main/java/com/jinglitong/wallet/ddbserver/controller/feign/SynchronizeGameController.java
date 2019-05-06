package com.jinglitong.wallet.ddbserver.controller.feign;

import com.jinglitong.wallet.ddbapi.feign.SynchronizeToGameFeignApi;
import com.jinglitong.wallet.ddbserver.service.CustomerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import javax.annotation.Resource;

@RestController
@RequestMapping("SynchronizeToGameFeign")
@Slf4j
public class SynchronizeGameController implements SynchronizeToGameFeignApi {


    @Resource
    CustomerService customerService;

    @PostMapping("SynchronizeInviteToGameFeign")
    @Override
    public Map SynchronizeInviteToGame(@RequestBody Map map) {
    	return customerService.SynchronizeInviteToGame(map);
        

    }

}