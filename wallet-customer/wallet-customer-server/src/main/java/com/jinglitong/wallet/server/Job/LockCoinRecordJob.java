//package com.jinglitong.wallet.server.Job;
//
//import com.jinglitong.wallet.server.service.*;
//import com.jinglitong.wallet.server.util.DateUtils;
//import com.jinglitong.wallet.server.service.*;
//import com.jinglitong.wallet.api.model.LockCoinRecord;
//import com.jinglitong.wallet.api.model.MainChain;
//import com.jinglitong.wallet.api.model.SubChain;
//import com.jinglitong.wallet.api.model.view.PaymentVO;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Component
//public class LockCoinRecordJob {
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Resource
//    private JingtongWalletService jingtongWalletService;
//
//    @Resource
//    private MainChainService mainChainService;
//
//    @Resource
//    private SubChainService subChainService;
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Resource
//    private CustomerAPIService apiService;
//
//    /**
//     * 锁仓到账
//     */
//    private static final String LOCK_COIN_RECORD_JOB_IDENTIFICATION ="lock_coin_record_job_iden";
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
//     * 任务开关
//     */
//    private int lockCoinJobOpen = 1;
//
//
//
//    @Resource
//    private LockCoinRecordService recordService;
//
//
//    /**
//     * 确定支付状态
//     */
//    public void confirmPayStatus(){
//        if(lockCoinJobOpen!=1){
//            return;
//        }
//
//        try{
//            if(null != redisTemplate.opsForValue().get(LOCK_COIN_RECORD_JOB_IDENTIFICATION) && (int)redisTemplate.opsForValue().get(LOCK_COIN_RECORD_JOB_IDENTIFICATION)==1){
//                logger.error("任务再跑着，下次吧！");
//            }else {
//                redisTemplate.opsForValue().set(LOCK_COIN_RECORD_JOB_IDENTIFICATION,1);
//                excueteTask();
//            }
//
//        }catch (Exception e){
//            logger.error("错误："+e.getMessage());
//            e.printStackTrace();
//        }finally {
//            //删除标识
//            redisTemplate.delete(LOCK_COIN_RECORD_JOB_IDENTIFICATION);
//        }
//
//    }
//
//    /**
//     * 执行任务
//     */
//    private void excueteTask(){
//        LockCoinRecord queryRecord = new LockCoinRecord();
//        queryRecord.setLockStatus(1); //查询正在锁的
//        int totalNum = recordService.queryLockCoinCount(queryRecord);
//        if(totalNum <=0){
//            logger.info("这次没有数据");
//            return;
//        }
//        List<LockCoinRecord> coinRecords = recordService.queryLockCoinRecords(queryRecord,0,query_per_size);
//        do{
//            for(LockCoinRecord record:coinRecords){
//                //查询状态
//                SubChain subChain = subChainService.selectByCoinId(record.getCoinId());
//                if(null == subChain || StringUtils.isEmpty(subChain.getChainId())){
//                    logger.error("找不到币：记录id =: "+record.getLockRecordId());
//                    continue;
//                }
//                //查询链信息
//                MainChain mainChain = new MainChain();
//                mainChain.setChainId(subChain.getChainId());
//                mainChain = mainChainService.getOneMainChain(mainChain);
//                if(mainChain.getHandleName().indexOf("swt")==-1){
//                    logger.error("目前只支持swt系列！");
//                    continue;
//                }
//
//                try{
//                    PaymentVO paymentVO = new PaymentVO();
//                    paymentVO.setChainId(mainChain.getChainId());
//                    paymentVO.setWalletId(record.getWalletId());
//                    paymentVO.setCustId(record.getCustId());
//                    paymentVO.setPayHash(record.getPayHash());
//                    Map<String,Object>  map = apiService.getTX(paymentVO);
//
//                    if((int)map.get("code") ==0 &&(Boolean) ((Map)map.get("data")).get("success")){
//                        record.setLockStatus(2);//锁仓成功
//                        record.setUpdateTime(DateUtils.getDateTime());
//                        recordService.updateLockCoinRecord(record);
//                    }else {
//                        logger.error("查询结果失败:"+record.getLockRecordId());
//                        record.setLockStatus(6); //锁仓失败
//                        record.setUpdateTime(DateUtils.getDateTime());
//                        recordService.updateLockCoinRecord(record);
//                    }
//
//                }catch (Exception e){
//                    logger.error(e.getMessage());
//                    e.printStackTrace();
//                }
//
//                coinRecords = recordService.queryLockCoinRecords(queryRecord,0,query_per_size);
//            }
//
//        }while (coinRecords.size()>0);
//    }
//
//
//    /**
//     * 获得ip
//     * @param url
//     * @return
//     */
//    private String getIP(String url) {
//        //使用正则表达式过滤，
//        String re = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
//        String str = "";
//        // 编译正则表达式
//        Pattern pattern = Pattern.compile(re);
//        // 忽略大小写的写法
//        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(url);
//        //若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
//        if (matcher.matches()) {
//            str = url;
//        } else {
//            String[] split2 = url.split(re);
//            if (split2.length > 1) {
//                String substring = url.substring(0, url.length() - split2[1].length());
//                str = substring;
//            } else {
//                str = split2[0];
//            }
//        }
//        return str;
//    }
//
//
//}
