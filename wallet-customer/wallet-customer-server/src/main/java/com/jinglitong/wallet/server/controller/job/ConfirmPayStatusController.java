package com.jinglitong.wallet.server.controller.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.feign.ConfirmPayStatusFeignApi;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.api.model.view.PaymentVO;
import com.jinglitong.wallet.server.service.CustomerAPIService;
import com.jinglitong.wallet.server.service.JingtongWalletService;
import com.jinglitong.wallet.server.service.MainChainService;
import com.jinglitong.wallet.server.service.PayInfoService;
import com.jinglitong.wallet.server.service.SellerService;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * Copyright (c) 2018, 井立通
 * All rights reserved.
 * 文件名称: ConfirmPayStatusController.java
 * 作        者: yxl 2018年8月9日
 * 创建时间: 2018年8月9日
 * 功能说明:查询支付订单是否到账
 */
@RequestMapping("PayStatusJob")
@RestController
@Slf4j
public class ConfirmPayStatusController implements ConfirmPayStatusFeignApi {

	@Resource
	private PayInfoService payInfoService;

	@Resource
	private JingtongWalletService jingtongWalletService;

	@Resource
	private SellerService sellerService;

	@Resource
	private MainChainService mainChainService;

    @Resource
    private CustomerAPIService apiService;
    
    /**
     * 每次查询的条数
     */
    @Value("${batchtransfer.querysize}")
    private int query_per_size = 1;
    
    @PostMapping("excueteTaskFeign")
	@Override
	public void excueteTask() {
    	log.info("查询订单状态开始");
    	PayInfo payInfo = new PayInfo();
        payInfo.setPayStatus(1);//申请支付
        int totalNum = payInfoService.getPayInfoCount(payInfo);
        if(totalNum <=0){
            log.info("这次没有数据");
            return;
        }
        List<PayInfo> payInfos = payInfoService.getPayInfoByPage(payInfo,0,query_per_size);

        do{
            for(PayInfo info:payInfos){
                //查询商家
                Seller seller = new Seller();
                seller.setSellerId(info.getSellerId());
                seller =  sellerService.queryOneSeller(seller);
                if(null == seller) {
                	 log.error("商家不存在");
                     continue;
                }
                //查询链信息
                MainChain mainChain = new MainChain();
                mainChain.setChainId(info.getChainId());
                mainChain = mainChainService.getOneMainChain(mainChain);
                if(mainChain.getHandleName().indexOf("swt")==-1){
                	info.setPayStatus(5);
                	payInfoService.updatePayInfo(info);
                    continue;
                }
                try{
                    PaymentVO paymentVO = new PaymentVO();
                    paymentVO.setChainId(mainChain.getChainId());
                    paymentVO.setWalletId(info.getWalletId());
                    paymentVO.setCustId(info.getCustId());
                    paymentVO.setPayHash(info.getTradeHash());
                    Map<String,Object> map = apiService.getTX(paymentVO);


                    if((int)map.get("code") ==0 &&(Boolean) ((Map)map.get("data")).get("success")){
                        info.setPayStatus(3);
                    }else {
                        info.setPayStatus(4);
                    }
                    info.setUpdateTime(new Date());
//                    info.setErrorMsg();
                    payInfoService.updatePayInfo(info);
                }catch (Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
                payInfos = payInfoService.getPayInfoByPage(payInfo,0,query_per_size);
            }

        }while (payInfos.size()>0);
        log.info("查询订单状态结束");
	}

}
