package com.jinglitong.wallet.job.task.threadpool;


import com.jinglitong.wallet.ddbapi.model.DdbShouldFrozen;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.service.GrowInteService;
import lombok.extern.slf4j.Slf4j;


/**
 * @author  fyy
 *
 * @date 2
 */
@Slf4j
public class AddIntegTask implements Runnable {


    private GrowInteService growInteService;
    private DdbShouldFrozen ddbShouldFrozen;


    public AddIntegTask(DdbShouldFrozen ddbShouldFrozen) {
        this.ddbShouldFrozen = ddbShouldFrozen;

        growInteService = (GrowInteService) ApplicationContextHelper.getIntegralAccountBean(GrowInteService.class);
    }


    @Override
    public void run() {
        try {
            log.info("=========本次转账消耗开始时间："+System.currentTimeMillis());
            growInteService.growIntege(ddbShouldFrozen);
            log.info("=========本次转账消耗结束时间："+System.currentTimeMillis());
        } catch (Exception e) {
            log.error("addInteTask thread error", e);
        }
    }
}
