//package com.jinglitong.wallet.server.Job;
//
//import com.jinglitong.wallet.api.model.MainChain;
//import com.jinglitong.wallet.api.model.PayInfo;
//import com.jinglitong.wallet.api.model.Seller;
//import com.jinglitong.wallet.api.model.view.PaymentVO;
//import com.jinglitong.wallet.server.service.*;
//import com.jinglitong.wallet.server.service.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class ConfirmPayStatusJob {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Resource
//    private RestTemplate restTemplate;
//
//    @Resource
//    private PayInfoService payInfoService;
//
//    @Resource
//    private JingtongWalletService jingtongWalletService;
//
//    @Resource
//    private SellerService sellerService;
//
//    @Resource
//    private MainChainService mainChainService;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//
//    @Resource
//    private CustomerAPIService apiService;
//
//    /**
//     * 任务开关
//     */
//    private int confirmPayStatusOpen = 0;
//
//    /**
//     * 确认支付是否到账通知
//     */
//    private static final String CONFIRM_PAY_STATUS_JOB_IDEN ="confirm_pay_status_job_iden";
//
//
//
//    /**
//     * 每次查询的条数
//     */
//    @Value("${batchtransfer.querysize}")
//    private int query_per_size = 1;
//
//    /**
//     * 确定支付状态
//     */
//    public void confirmPayStatus(){
//        if(confirmPayStatusOpen !=1){
//            return;
//        }
//        try{
//            if(null != redisTemplate.opsForValue().get(CONFIRM_PAY_STATUS_JOB_IDEN) && (int)redisTemplate.opsForValue().get(CONFIRM_PAY_STATUS_JOB_IDEN)==1){
//                logger.error("任务再跑着，下次吧！");
//            }else {
//                redisTemplate.opsForValue().set(CONFIRM_PAY_STATUS_JOB_IDEN,1);
//                excueteTask();
//            }
//
//        }catch (Exception e){
//            logger.error("错误："+e.getMessage());
//            e.printStackTrace();
//        }finally {
//            //删除标识
//            redisTemplate.delete(CONFIRM_PAY_STATUS_JOB_IDEN);
//        }
//
//    }
//
//    /**
//     * 执行任务
//     */
//    private void excueteTask(){
//        PayInfo payInfo = new PayInfo();
//        payInfo.setPayStatus(1);//申请支付
//        int totalNum = payInfoService.getPayInfoCount(payInfo);
//        if(totalNum <=0){
//            logger.info("这次没有数据");
//            return;
//        }
//        List<PayInfo> payInfos = payInfoService.getPayInfoByPage(payInfo,0,query_per_size);
//
//        do{
//            for(PayInfo info:payInfos){
//                //查询商家
//                Seller seller = new Seller();
//                seller.setSellerId(info.getSellerId());
//                seller =  sellerService.queryOneSeller(seller);
//
//                //查询链信息
//                MainChain mainChain = new MainChain();
//                mainChain.setChainId(info.getChainId());
//                mainChain = mainChainService.getOneMainChain(mainChain);
//                if(mainChain.getHandleName().indexOf("swt")==-1){
//                    logger.error("目前只支持swt系列！");
//                    continue;
//                }
//                try{
//                    PaymentVO paymentVO = new PaymentVO();
//                    paymentVO.setChainId(mainChain.getChainId());
//                    paymentVO.setWalletId(info.getWalletId());
//                    paymentVO.setCustId(info.getCustId());
//                    paymentVO.setPayHash(info.getTradeHash());
//                    Map<String,Object> map = apiService.getTX(paymentVO);
//
//
//                    if((int)map.get("code") ==0 &&(Boolean) ((Map)map.get("data")).get("success")){
//                        info.setPayStatus(3);
//                    }else {
//                        info.setPayStatus(4);
//                    }
//                    info.setUpdateTime(new Date());
////                    info.setErrorMsg();
//                    payInfoService.updatePayInfo(info);
//                }catch (Exception e){
//                    logger.error(e.getMessage());
//                    e.printStackTrace();
//                }
//                payInfos = payInfoService.getPayInfoByPage(payInfo,0,query_per_size);
//            }
//
//        }while (payInfos.size()>0);
//    }
//
//
//}
