package com.jinglitong.wallet.ddbkjserver.common.spring;

import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.service.CustomerService;
import com.jinglitong.wallet.ddbkjserver.service.HandlerOrderService;
import com.jinglitong.wallet.ddbkjserver.service.HandlerRegService;
import com.jinglitong.wallet.ddbkjserver.service.InitBonusSystemService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitApplicationRunner implements CommandLineRunner {


    @Autowired
    private HandlerOrderService handlerOrder;

    @Autowired
    private HandlerRegService handlerReg;

    @Autowired
    CustomerService customerService;
    
    @Autowired
	InitBonusSystemService initBonusSystemService;
    
    @Override
    public void run(String... args) {
        log.info("begin init tree");
        //初始化用户树结构
        customerService.initUserTree();
        initBonusSystemService.getDdbRewardRuleDetail();
        initBonusSystemService.getDdbRewardGfRuleDetail();
        /*
        KJNotice notice1 = new KJNotice();
        notice1.setRuleId("16");
        notice1.setType("1");
        notice1.setGfValue(100000);
        notice1.setId(222222);
        notice1.setFlowId("222222");
        notice1.setShopTrade("222222");
        notice1.setCustId("b3f3c306b6ac4dc09b62eb9e7794634b");
        notice1.setProductNum("1");
        try {
            ConstantDict.ORDER_CONTROL.put(notice1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        while (true) {
            try {
                KJNotice notice = ConstantDict.ORDER_CONTROL.take();
                if(notice.getType().equals("1")||notice.getType().equals("2")) {
                	//订单或育苗购买
                    handlerOrder.handlerOrder(notice);
                }else if(notice.getType().equals("3")){
                	//新增注册
                    handlerReg.handlerReg(notice);
                }
            } catch (InterruptedException e) {
                log.error("ORDER_CONTROL ERROR",e);
            }
        }
    }

}
