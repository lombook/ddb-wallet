//package com.jinglitong.wallet.server.Job;
//
//import com.jinglitong.wallet.server.common.BatchTransfer.BatchTransferConstants;
//import com.jinglitong.wallet.server.util.RSASignature;
//import com.jinglitong.wallet.api.model.PayInfo;
//import com.jinglitong.wallet.api.model.Seller;
//import com.jinglitong.wallet.server.service.JingtongWalletService;
//import com.jinglitong.wallet.server.service.MainChainService;
//import com.jinglitong.wallet.server.service.PayInfoService;
//import com.jinglitong.wallet.server.service.SellerService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.*;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Component
//public class NotifySellerJob {
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
//    @Value("${payPrivateKey}")
//    private String privateKey;
//    @Value("${payPubKey}")
//    private String pubKey;
//
//
//    /**
//     * 通知
//     */
//    private static final String NOTIFY_SELLER_JOB_IDENTIFICATION ="notify_seller_job_ident";
//
//
//
//
//
//
//    /**
//     * 任务开关
//     */
//    private int notifySellerOpen = 0;
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
//        if(notifySellerOpen != 1){
//            return;
//        }
//        try{
//            if(null != redisTemplate.opsForValue().get(NOTIFY_SELLER_JOB_IDENTIFICATION) && (int)redisTemplate.opsForValue().get(NOTIFY_SELLER_JOB_IDENTIFICATION)==1){
//                logger.error("任务再跑着，下次吧！");
//            }else {
//                redisTemplate.opsForValue().set(NOTIFY_SELLER_JOB_IDENTIFICATION,1);
//                excueteTask();
//            }
//
//        }catch (Exception e){
//            logger.error("错误："+e.getMessage());
//            e.printStackTrace();
//        }finally {
//            //删除标识
//            redisTemplate.delete(NOTIFY_SELLER_JOB_IDENTIFICATION);
//        }
//
//    }
//
//    /**
//     * 执行任务
//     */
//    private void excueteTask(){
//        int totalNum = payInfoService.getHavedPayCountInfoByExample();
//        if(totalNum <=0){
//            logger.info("这次没有数据");
//            return;
//        }
//        List<PayInfo> payInfos = payInfoService.getHavedPayInfoByExample(0,query_per_size);
//        do{
//            for(PayInfo info:payInfos){
//                //查询商家
//                Seller seller = new Seller();
//                seller.setSellerId(info.getSellerId());
//                seller =  sellerService.queryOneSeller(seller);
//
//
//                try{
//                    String notiryUrl = seller.getAsynNotify();
//
//                    HttpHeaders httpHeaders = new HttpHeaders();
//                    httpHeaders.set(BatchTransferConstants.APP_KEY,BatchTransferConstants.APP_KEY_VALUE);
//                    String timstamp = System.currentTimeMillis()+"";
//                    httpHeaders.set(BatchTransferConstants.TIMESTAMP,timstamp);
//                    String signData = "appkey="+BatchTransferConstants.APP_KEY_VALUE+";timestamp="+timstamp;
//                    httpHeaders.set(BatchTransferConstants.SIGN, RSASignature.sign(signData.getBytes(),privateKey));
//
//                    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//                    httpHeaders.setContentType(type);
//
//
//
//                    MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
//                    params.add("orderNo",info.getOrderNo());
//                    params.add("sellerOrderNo",info.getSellerOrderNo());
//                    params.add("payStatus",info.getPayStatus()+"");
//                    HttpEntity requestEntity = new HttpEntity(params, httpHeaders);
//
//                    ResponseEntity<String> response = restTemplate.postForEntity(notiryUrl,requestEntity, String.class);
//                    if(response.getBody().equalsIgnoreCase("1")){
//                        info.setNoticeTime(null == info.getNoticeTime()?1:info.getNoticeTime()+1);
//                        info.setNoticeStatus(1); //通知成功
//                    }else {
//                        info.setNoticeTime(null == info.getNoticeTime()?1:info.getNoticeTime()+1);
//                        info.setNoticeStatus(2); //未收到反馈
//                    }
//                    payInfoService.updatePayInfo(info);
//
//                }catch (Exception e){
//                    logger.error(e.getMessage());
//                    e.printStackTrace();
//                }
//                payInfos = payInfoService.getHavedPayInfoByExample(0,query_per_size);
//            }
//
//        }while (payInfos.size()>0);
//    }
//
//
//}
