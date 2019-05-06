package com.jinglitong.wallet.ddbkjserver.controller.Task;


import com.jinglitong.wallet.ddbapi.model.DdbGrowSeed;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbGrowSeedMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: GrowSeedJob.java
 * 作        者: wyw
 * 创建时间: 2018年10月23日
 * 功能说明:扫描育苗表并保存到队列定时任务
 */
@Component
@Slf4j
public class GrowSeedToQueneTask {

    /**
     * 任务开关
     */
    @Value("${growSeedJob.open}")
    private int growSeedJob = 1;

    @Autowired
    private DdbGrowSeedMapper ddbGrowSeedMapper;
    
    /**
     * 确定状态
     */
    @Scheduled(cron = "${growSeedJob.cron}")
    public void confirmPayStatus(){
        if(growSeedJob!=1){
            return;
        }
        try{
           //查询是否有未处理的育苗订单
        	List<DdbGrowSeed> list = getNewGrowSeeds();
           //结果列表不为空且有值，则保存到队列中
        	if(list!=null&&list.size()>0){
        		log.info("new grow not null, count is " + list.size());
        		//flowId type custId amount
        		for (DdbGrowSeed ddbGrowSeed : list) {
        			KJNotice notice = new KJNotice();
					notice.setFlowId(ddbGrowSeed.getZid());
					notice.setCustId(ddbGrowSeed.getCustId());
					//育苗类型为2
					notice.setType("2");
					Integer amount = ddbGrowSeed.getAmount();
					if(amount==null||amount.equals("")){
						amount = 0;
					}
					notice.setAmount(new BigDecimal(amount));
					//保存到队列
        			ConstantDict.ORDER_CONTROL.add(notice);
				}
        	}else {
        		log.info("new grow seed is null");
        	}
        }catch (Exception e){
        	//打印错误信息
            log.error("error: "+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 获取未处理的育苗列表
     * @return
     */
    public List<DdbGrowSeed> getNewGrowSeeds(){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("state", 0);
    	List<DdbGrowSeed> list = ddbGrowSeedMapper.getNewGrowSeedsList(map);
    	return list;
    }
}
