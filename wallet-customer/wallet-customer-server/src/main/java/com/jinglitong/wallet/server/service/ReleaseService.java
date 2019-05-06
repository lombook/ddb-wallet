package com.jinglitong.wallet.server.service;


import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.server.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.server.mapper.*;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class ReleaseService {

    Boolean flag = false;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MainChainMapper mainChainMapper;

    private static final String password = "123456";

    public ConcurrentHashMap<String,Integer> concurrentHashMap = new ConcurrentHashMap();

    @Autowired
    private FrozenReleaseRuleMapper frozenReleaseRuleMapper;

    @Autowired
    private FrozenReleaseLogFailMapper frozenReleaseLogFailMapper;
    
    @Autowired
    private FrozenReleaseLogsMapper frozenReleaseLogsMapper;
    
    @Autowired
    private FrozenReleaseAffirmMapper frozenReleaseAffirmMapper;

    @Autowired
    private FrozenReleaseDetailRuleMapper frozenReleaseDetailRuleMapper;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private FrozenReleaseExcelSourceMapper frozenReleaseExcelSourceMapper;

    @Autowired
    private FrozenReleaseExcelSourceAmountMapper frozenReleaseExcelSourceReleaseAmountMapper;

    @Value("${beforeTime}")
    private long  timebefore;

    private Boolean chekcFlag = true;

public Boolean checkRelease(){
      Date now = new Date();
        Date beforetime = new Date(now.getTime() - 60000 * timebefore); //10分钟前的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        String nowTime = dateFormat.format(beforetime);
        List<FrozenReleaseAffirm> affirmlist = frozenReleaseAffirmMapper.selectBeforTime(nowTime);
        logger.info("check指定时间之前的affirm");
        while (!chekcFlag){
            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        chekcAffirem(affirmlist);
    //对全部失败的发邮件
    for(Map.Entry<String,Integer> entry : concurrentHashMap.entrySet()){
        List<FrozenReleaseLogs>  list=frozenReleaseLogsMapper.selectByDetailIdAndTrue(entry.getKey());
        if(list == null || list.size() <= 0){
           // aliCloudMailService.sendMail(mailAddress,"释放提示","你好：失败量达到上限，请检查账户");
        }
        concurrentHashMap.remove(entry.getKey());
    }
    return true;
}
    //对每个affirm进行检查
    Boolean chekcAffirem(List<FrozenReleaseAffirm> affirmlist){
        for (FrozenReleaseAffirm fraf:affirmlist) {
            //String walletId = fraf.getWalletId();
            //Wallet wallet = walletMapper.selectByWalletId(walletId);
            FrozenReleaseDetailRule frozenReleaseDetailRule = frozenReleaseDetailRuleMapper.selecByDetailId(fraf.getDetailId());
            FrozenReleaseRule frozenReleaseRule = frozenReleaseRuleMapper.selectByRuleId(frozenReleaseDetailRule.getRuleId());
            LPayResVO resVO = new LPayResVO();
            resVO.setChainId(frozenReleaseRule.getChianId());
            resVO.setTxHash(fraf.getPayHash());
            ChainService service = getChainService(frozenReleaseRule.getChianId());
            service.getTx(resVO);
            if (resVO.getResCode().equals(0)) {
                logger.debug("验证"+fraf.getId());
                //判断是否成功
                //成功
                Map<String, Object> resMap =resVO.getResMap();
                Object success = resMap.get("success");
                if("true".equals(success.toString())){
                    releaseSuccess(fraf);
                }else{
                    //失败
                    FrozenReleaseLogs frozenReleaseLogs2 = frozenReleaseLogsMapper.selectByLogId(fraf.getLogId());
                    FrozenReleaseLogFail frozenReleaseLogs1 = new FrozenReleaseLogFail();
                    frozenReleaseLogs1.setZid(UuidUtil.getUUID());
                    frozenReleaseLogs1.setLogId(frozenReleaseLogs2.getLogId());
                    frozenReleaseLogs1.setCreateTime(DateUtils.getDateTime());
                    frozenReleaseLogs1.setLedgerAdress(frozenReleaseLogs2.getLedgerAdress());
                    frozenReleaseLogs1.setFailState(1);
                    frozenReleaseLogs1.setSendAmount(frozenReleaseLogs2.getSendAmount());
                    frozenReleaseLogs1.setTargetAddress(frozenReleaseLogs2.getTargetAddress());
                    frozenReleaseLogs1.setUpdateTime(frozenReleaseLogs2.getUpdateTime());
                    frozenReleaseLogs1.setDetailId(frozenReleaseLogs2.getDetailId());
                    frozenReleaseLogFailMapper.insert(frozenReleaseLogs1);
                    FrozenReleaseLogs frozenReleaseLogs = frozenReleaseLogsMapper.selectByLogId(fraf.getLogId());
                    FrozenReleaseLogs frozenReleaseLo = new FrozenReleaseLogs();
                    frozenReleaseLo.setId(frozenReleaseLogs.getId());
                    frozenReleaseLo.setLogId(fraf.getLogId());
                    String dateTime = DateUtils.getDateTime();
                    frozenReleaseLo.setConfirmTime(dateTime);
                    frozenReleaseLo.setLogStatus(1);
                    frozenReleaseLo.setUpdateTime(dateTime);
                    int i3 = frozenReleaseLogsMapper.updateByPrimaryKeySelective(frozenReleaseLo);
                    if(i3<0){
                        logger.error("日志支付确认失败记录号："+fraf.getLogId()+"success");
                    }else{
                        int i4 = frozenReleaseAffirmMapper.deleteByPrimaryKey(fraf.getId());
                        if(i4 <=0){
                            logger.error("日志支付确认表删除失败："+fraf.getId()+"159");
                        }
                    }
                }
            }else {
                logger.debug("验证出错"+resVO.getMessage());
            }
        }
        return true;
    }

    //释放
    public Boolean releaseAmount() {
        flag = true;
        List<FrozenReleaseDetailRule> list = frozenReleaseDetailRuleMapper.selectTodayRules();
        if(list.size()>0){
            for (FrozenReleaseDetailRule detailRule : list) {
                detailRelease(detailRule,"releaseAmount");
            }


        }
        flag = false;
        return true;
    }
    //保存日志和确认表
    @Transactional
    private Boolean insertData(FrozenReleaseLogs frozenReleaseLogs1, FrozenReleaseAffirm frozenReleaseAffirm) throws Exception{
        int insert = frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
        int insert1 = frozenReleaseAffirmMapper.insert(frozenReleaseAffirm);
        if(insert == 1 && insert1 == 1)
            return true;
        else
            return false;
    }

    private ChainService getChainService(String chainId) {
        MainChain mainChain = mainChainMapper.selectByChainId(chainId);
        if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) )
            return null;
        ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
        return chainService;

    }

    //查询指定规则指定钱包释放成功总金额
    private String selectReleaseMoney(String walletId, String ruleId){
        BigDecimal amount = new BigDecimal("0");
        List<FrozenReleaseLogs> list = frozenReleaseLogsMapper.selectByWRid(walletId,ruleId);
        if(list.size() > 0)
            for (FrozenReleaseLogs f:list) {
                amount = amount.add(new BigDecimal(f.getSendAmount()));
            }
        return amount.toString();
    }

    //针对一个规则释放
    private Boolean detailRelease(FrozenReleaseDetailRule detailRule, String type){
        concurrentHashMap.put(detailRule.getDetailId(),detailRule.getRule().getSourceList().size());
        FrozenReleaseRule rule = detailRule.getRule();
        FrozenReleaseLogs frozenReleaseLogsin = null;
        if(rule.getSourceList() != null ||rule.getSourceList().size()>0){
            for (FrozenReleaseExcelSource sourec: rule.getSourceList()) {
                FrozenReleaseExcelSource soso= frozenReleaseExcelSourceMapper.selectBySourceId(sourec.getSourceId());
                if(soso.getSourceStatus() == 3){
                    continue;
                }
               List<FrozenReleaseAffirm> affirms = frozenReleaseAffirmMapper.selectByWalletIdAndDetailId(sourec.getWalletId(),sourec.getRuleId());
                chekcFlag = false;
                chekcAffirem(affirms);
                chekcFlag = true;
                String walletId = sourec.getWalletId();
                logger.info("释放："+walletId);
                //查询失败记录中中改规则下该钱包失败的记录
                List<FrozenReleaseLogFail> frozenReleaseLogFails = selectByWalletIdRuleId(walletId, rule.getRuleId());
                BigDecimal padAmount = BigDecimal.ZERO;
                if(frozenReleaseLogFails.size()>0){
                    for (FrozenReleaseLogFail ffail: frozenReleaseLogFails) {
                        padAmount =padAmount.add(new BigDecimal(ffail.getSendAmount()));
                    }
                }
                String amount = sourec.getAmount();
                Integer proportion = detailRule.getProportion();//应释放的%比
                Integer leftProportion = detailRule.getLeftProportion();//剩余的%比
                int nowProportion = detailRule.getLeftProportion() - proportion;//释放后剩余的%比
                BigDecimal bigDecimal = new BigDecimal(amount);
                BigDecimal multiply = bigDecimal.multiply(new BigDecimal(proportion)).divide(new BigDecimal(1000000));//应释放金额
                multiply= multiply.add(padAmount);//加上失败的金额
                BigDecimal subsecondamount = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(multiply.toString()));
                //调用转账接口
                ChainService service = getChainService(rule.getChianId());
                LPaymentVO lvo = new LPaymentVO();
                lvo.setAmount(multiply.toString());
                lvo.setCurrency(rule.getSubChain().getCurrency().toUpperCase());
                lvo.setDestAddress(sourec.getAddress());
                lvo.setFromAddress(rule.getPublicKey());
                lvo.setMemo("冻结资产释放");
                lvo.setPpwd(password);
                lvo.setCustid(rule.getRuleId());
                lvo.setSecretkey(rule.getPayPasswd());
                lvo.setUuid(UuidUtil.getUUID());
                if(rule.getSubChain() != null){
                    if(rule.getSubChain().getBaseChain() == null){
                        if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                            //设置银冠
                            lvo.setIssuer(rule.getSubChain().getTokenAddress());
                        }
                    }else {

                        if( rule.getSubChain().getBaseChain() !=true)
                            if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                                //设置银冠
                                lvo.setIssuer(rule.getSubChain().getTokenAddress());
                            }
                    }
                }

                String paymentHash = lvo.getPaymentHash();
                //封装数据
                FrozenReleaseLogs frozenReleaseLogs1 = new FrozenReleaseLogs();
                //frozenReleaseLogs1.setConfirmTime();
                String dateTime = DateUtils.getDateTime();
                frozenReleaseLogs1.setCreateTime(dateTime);
                frozenReleaseLogs1.setLedgerAdress(rule.getPublicKey());
                String logId = UuidUtil.getUUID();
                frozenReleaseLogs1.setLogId(logId);
                frozenReleaseLogs1.setLogStatus(2);
                frozenReleaseLogs1.setPadAmount(padAmount.toString());//上次失败填充的钱数
                frozenReleaseLogs1.setPayHash(paymentHash);
                frozenReleaseLogs1.setPayTime(dateTime);
                String ss = selectReleaseMoney(sourec.getWalletId(), sourec.getRuleId());
                BigDecimal subtract = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(ss));
                frozenReleaseLogs1.setPreSendAmount(subtract.toString());
                frozenReleaseLogs1.setSendAmount(lvo.getAmount());
                frozenReleaseLogs1.setProportion(proportion);
                frozenReleaseLogs1.setSourceAmount(sourec.getAmount());
                BigDecimal multiply1 = subtract.subtract(multiply);
                frozenReleaseLogs1.setSufSendAmount(multiply1.toString());//釋放完剩余
                frozenReleaseLogs1.setTargetAddress(sourec.getAddress());
                frozenReleaseLogs1.setUpdateTime(dateTime);
                frozenReleaseLogs1.setWalletId(sourec.getWalletId());
                frozenReleaseLogs1.setDetailId(detailRule.getDetailId());



                FrozenReleaseAffirm frozenReleaseAffirm = new FrozenReleaseAffirm();
                frozenReleaseAffirm.setCreateTime(dateTime);
                String detailId = UuidUtil.getUUID();
                frozenReleaseAffirm.setDetailId(detailRule.getDetailId());
                frozenReleaseAffirm.setWalletId(sourec.getWalletId());
                frozenReleaseAffirm.setPayHash(paymentHash);
                frozenReleaseAffirm.setLogId(logId);
                service.payment(lvo, rule.getChianId());
                logger.info("释放日志：钱包ID：",frozenReleaseLogs1.getWalletId()+"转账金额："+frozenReleaseLogs1.getSendAmount()+"payCode:"+lvo.getResCode());
                for (FrozenReleaseLogFail frlf:frozenReleaseLogFails) {
                    frozenReleaseLogFailMapper.deleteByPrimaryKey(frlf.getId());
                }
                if (lvo.getResCode().equals(0)){
                    logger.debug("释放转账成功"+lvo.toString());
                    paymentHash = lvo.getPaymentHash();
                    frozenReleaseLogs1.setPayHash(paymentHash);
                    frozenReleaseAffirm.setPayHash(paymentHash);
                    Boolean aBoolean =false;
                    try {
                        aBoolean = insertData(frozenReleaseLogs1, frozenReleaseAffirm);
                    }catch (Exception e){
                        logger.error("释放日志入库失败"+paymentHash);
                        logger.debug("释放错误信息："+e);
                    }
                    if(aBoolean){
                        logger.debug("释放记录入库成功"+paymentHash);
                        frozenReleaseLogsin = null;
                        FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                        frozenReleaseExcelSource.setSourceId(sourec.getSourceId());
                        frozenReleaseExcelSource.setSourceStatus(1);
                        int i1 = 1;
                        if(sourec.getSourceStatus() == 0){
                            i1 = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
                        }
                        if(i1 <=0){
                            logger.error("用户状态修改失败1"+sourec.getSourceId());
                        }
                        //修改规则正在执行状态
                        Integer frStatus = rule.getFrStatus();
                        if(frStatus == 0){
                            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                            frozenReleaseRule.setRuleId(rule.getRuleId());
                            frozenReleaseRule.setFrStatus(1);
                            int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                            if(i <= 0){
                                logger.error("大规则修改状态失败1"+rule.getRuleId());
                            }
                        }
                        Integer detailStatus = detailRule.getDetailStatus();
                        if(detailStatus == 0){
                            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                            frozenReleaseDetailRule.setDetailStatus(1);
                            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                            if(i <= 0){
                                logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                            }
                        }
                    }else{
                        logger.error("释放日志入库失败hash"+paymentHash);
                    }
                   // releaseSuccess(frozenReleaseAffirm);//返回hash即是成功，对成功数据进行处理
                    logger.info("定时释放+amount");
                } else {
                    frozenReleaseLogs1.setLogStatus(1);
                    frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
                    String message = lvo.getMessage();
                    logger.error("释放转账失败添加进错误日志");
                    FrozenReleaseLogFail frozenReleaseLogsfyy = new FrozenReleaseLogFail();
                    frozenReleaseLogsfyy.setZid(UuidUtil.getUUID());
                    frozenReleaseLogsfyy.setCreateTime(DateUtils.getDateTime());
                    frozenReleaseLogsfyy.setLedgerAdress(frozenReleaseLogs1.getLedgerAdress());
                    frozenReleaseLogsfyy.setLogId(frozenReleaseLogs1.getLogId());
                    frozenReleaseLogsfyy.setFailState(2);
                    frozenReleaseLogsfyy.setSendAmount(frozenReleaseLogs1.getSendAmount());
                    frozenReleaseLogsfyy.setTargetAddress(frozenReleaseLogs1.getTargetAddress());
                    frozenReleaseLogsfyy.setUpdateTime(frozenReleaseLogs1.getUpdateTime());
                    frozenReleaseLogsfyy.setDetailId(frozenReleaseLogs1.getDetailId());
                    frozenReleaseLogFailMapper.insert(frozenReleaseLogsfyy);
                    //修改规则正在执行状态
                    Integer frStatus = rule.getFrStatus();
                    if(frStatus == 0){
                        FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                        frozenReleaseRule.setRuleId(rule.getRuleId());
                        frozenReleaseRule.setFrStatus(1);
                        int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                        if(i <= 0){
                            logger.error("大规则修改状态失败1"+rule.getRuleId());
                        }
                    }
                    Integer detailStatus = detailRule.getDetailStatus();
                    if(detailStatus == 0){
                        FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                        frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                        frozenReleaseDetailRule.setDetailStatus(1);
                        int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                        if(i <= 0){
                            logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                        }
                    }
                }

            }
        }else {
            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
            frozenReleaseDetailRule.setDetailStatus(2);
            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
            frozenReleaseRule.setRuleId(rule.getRuleId());
            frozenReleaseRule.setFrStatus(3);
             i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
        }

        //规则执行完成
        FrozenReleaseDetailRule frozenReleaseDetailRule2 = frozenReleaseDetailRuleMapper.selecByDetailId(detailRule.getDetailId());
        Integer detailStatus = frozenReleaseDetailRule2.getDetailStatus();
        if(detailStatus == 1){
            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
            frozenReleaseDetailRule.setDetailStatus(2);
            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
            if(i <= 0){
                logger.debug("小规则状态修改失败2");
                return false;
            }
        }
        FrozenReleaseRule frozenReleaseRule1 = frozenReleaseRuleMapper.selectByRuleId(rule.getRuleId());
        Integer frStatus = frozenReleaseRule1.getFrStatus();
        //查询detailrule
        FrozenReleaseDetailRule frozenReleaseDetailRule1 = frozenReleaseDetailRuleMapper.selecByDetailId(detailRule.getDetailId());
        if(frStatus == 1 && frozenReleaseDetailRule1.getLeftProportion() == 0){
            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
            frozenReleaseRule.setRuleId(rule.getRuleId());
            frozenReleaseRule.setFrStatus(2);
            int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
            if(i <= 0){
                logger.error("大规则修改状态失败2");
                return false;
            }
            List<FrozenReleaseExcelSource> sourceList = rule.getSourceList();
            for ( FrozenReleaseExcelSource froze:sourceList) {

            }
        }else {
            //添加匀速详细规则
            if(rule.getFrType() == 0){
                FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                String dateTime = DateUtils.getDateTime();
                frozenReleaseDetailRule.setAvgDays(detailRule.getAvgDays());
                frozenReleaseDetailRule.setCreateTime(dateTime);
                frozenReleaseDetailRule.setDetailId(UuidUtil.getUUID());
                frozenReleaseDetailRule.setDetailStatus(0);
                frozenReleaseDetailRule.setAppId(detailRule.getAppId());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String executTime = detailRule.getExecutTime();
                Date date = null;
                try {
                    date=simpleDateFormat.parse(executTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date1 = DateUtils.addDays(date, detailRule.getAvgDays());
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date1);
                frozenReleaseDetailRule.setExecutTime(dateStr);
                if(detailRule.getLeftProportion() < detailRule.getProportion()){
                    frozenReleaseDetailRule.setProportion(detailRule.getLeftProportion());
                    frozenReleaseDetailRule.setLeftProportion(0);
                }else{
                    frozenReleaseDetailRule.setProportion(detailRule.getProportion());
                    frozenReleaseDetailRule.setLeftProportion(detailRule.getLeftProportion() - detailRule.getProportion());
                }
                frozenReleaseDetailRule.setRuleId(detailRule.getRuleId());
                frozenReleaseDetailRule.setUpdateTime(dateTime);
                int insert = frozenReleaseDetailRuleMapper.insert(frozenReleaseDetailRule);
                if(insert < 0){
                    logger.error("匀速增加detail规则失败"+detailRule.getDetailId());
                }
            }
        }
    return true;
    }


    private List<FrozenReleaseLogFail> selectByWalletIdRuleId(String walletId, String ruleId){
        List<FrozenReleaseLogFail> frozenList = frozenReleaseLogFailMapper.selectByWalletIdAndRuleId(walletId,ruleId);
        return frozenList;
    }
    //处理错误前的数据
    public Boolean beforeFail(){
        flag = true;
        //處理錯誤數據
       List<FrozenReleaseDetailRule> releaseRules = frozenReleaseDetailRuleMapper.selectByState(1);
       if(releaseRules != null && releaseRules.size() >0){
           for (FrozenReleaseDetailRule frozenReleaseDetailRule : releaseRules) {
               Weekend<FrozenReleaseLogs> weekend = Weekend.of(FrozenReleaseLogs.class);
               WeekendCriteria<FrozenReleaseLogs, Object> criteria = weekend.weekendCriteria();
               criteria.andEqualTo(FrozenReleaseLogs::getDetailId, frozenReleaseDetailRule.getDetailId());
               //frozenReleaseLogsMapper.selectOneByExample(weekend);
               Integer count = frozenReleaseExcelSourceMapper.selectCountByRuleId(frozenReleaseDetailRule.getRuleId());
               List<FrozenReleaseLogs> frozenReleaseLogs1 = frozenReleaseLogsMapper.selectByExample(weekend);
               if(frozenReleaseLogs1 == null || frozenReleaseLogs1.size() < count){
                   detailRelease(frozenReleaseDetailRule,frozenReleaseLogs1,"beforeFail");
              }else{
                   if(frozenReleaseLogs1.size() == count){

                   }
               }
           }
       }
        //執行未執行的釋放規則
        List<FrozenReleaseDetailRule> releaseRuleList = frozenReleaseDetailRuleMapper.selectByState(0);
        if(releaseRuleList != null && releaseRuleList.size()>0){
            for (FrozenReleaseDetailRule detailRule : releaseRuleList) {
                detailRelease(detailRule,null,"releaseAmount");
            }
        }
        flag = false;
        return true;
    }

    //處理過期的释放
    private Boolean detailRelease(FrozenReleaseDetailRule detailRule, List<FrozenReleaseLogs> frozenReleaselist, String type) {
        concurrentHashMap.put(detailRule.getDetailId(),detailRule.getRule().getSourceList().size());
        FrozenReleaseRule rule = detailRule.getRule();
        FrozenReleaseLogs frozenReleaseLogsin = null;
        if(rule.getSourceList().size()>0)
            for (FrozenReleaseExcelSource sourec: rule.getSourceList()) {
                FrozenReleaseExcelSource soso= frozenReleaseExcelSourceMapper.selectBySourceId(sourec.getSourceId());
                if(soso.getSourceStatus() == 3){
                    continue;
                }
                List<FrozenReleaseAffirm> affirms = frozenReleaseAffirmMapper.selectByWalletIdAndDetailId(sourec.getWalletId(),sourec.getRuleId());
                chekcFlag = false;
                chekcAffirem(affirms);
                chekcFlag = true;
            logger.info("before释放："+sourec.getWalletId());
            if(frozenReleaselist != null ){
                Boolean falg = false;
                for (FrozenReleaseLogs frozenReleaseLogs:frozenReleaselist) {
                    if(frozenReleaseLogs.getWalletId().equals(sourec.getWalletId())){
                        falg = true;
                        break;
                    }
                }
                if(!falg){
                    String walletId = sourec.getWalletId();
                    //查询失败记录中中改规则下该钱包失败的记录
                    List<FrozenReleaseLogFail> frozenReleaseLogFails = selectByWalletIdRuleId(walletId, rule.getRuleId());
                    BigDecimal padAmount = BigDecimal.ZERO;
                    if(frozenReleaseLogFails.size()>0){
                        for (FrozenReleaseLogFail ffail: frozenReleaseLogFails) {
                            padAmount =padAmount.add(new BigDecimal(ffail.getSendAmount()));
                        }
                    }

                    String amount = sourec.getAmount();
                    Integer proportion = detailRule.getProportion();//应释放的%比
                    Integer leftProportion = detailRule.getLeftProportion();//剩余的%比
                    int nowProportion = detailRule.getLeftProportion() - proportion;//释放后剩余的%比
                    BigDecimal bigDecimal = new BigDecimal(amount);
                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(proportion)).divide(new BigDecimal(1000000));//应释放金额
                    multiply= multiply.add(padAmount);//加上失败的金额
                    BigDecimal subsecondamount = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(multiply.toString()));
                    //调用转账接口
                    ChainService service = getChainService(rule.getChianId());
                    LPaymentVO lvo = new LPaymentVO();
                    lvo.setAmount(multiply.toString());
                    lvo.setCurrency(rule.getSubChain().getCurrency().toUpperCase());
                    lvo.setDestAddress(sourec.getAddress());
                    lvo.setFromAddress(rule.getPublicKey());
                    lvo.setMemo("冻结资产释放");
                    lvo.setPpwd(password);
                    lvo.setCustid(rule.getRuleId());
                    lvo.setSecretkey(rule.getPayPasswd());
                    lvo.setUuid(UuidUtil.getUUID());
                    if(rule.getSubChain() != null){
                        if(rule.getSubChain().getBaseChain() == null){
                            if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                                //设置银冠
                                lvo.setIssuer(rule.getSubChain().getTokenAddress());
                            }
                        }else {

                            if( rule.getSubChain().getBaseChain() !=true){
                                if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                                    //设置银冠
                                    lvo.setIssuer(rule.getSubChain().getTokenAddress());
                                }
                            }
                        }
                    }

                    String paymentHash = lvo.getPaymentHash();
                    //封装数据
                    FrozenReleaseLogs frozenReleaseLogs1 = new FrozenReleaseLogs();
                    //frozenReleaseLogs1.setConfirmTime();
                    String dateTime = DateUtils.getDateTime();
                    frozenReleaseLogs1.setCreateTime(dateTime);
                    frozenReleaseLogs1.setLedgerAdress(rule.getPublicKey());
                    String logId = UuidUtil.getUUID();
                    frozenReleaseLogs1.setLogId(logId);
                    frozenReleaseLogs1.setLogStatus(2);
                    frozenReleaseLogs1.setPadAmount(padAmount.toString());//上次失败填充的钱数
                    frozenReleaseLogs1.setPayHash(paymentHash);
                    frozenReleaseLogs1.setPayTime(dateTime);
                    String ss = selectReleaseMoney(sourec.getWalletId(), sourec.getRuleId());
                    BigDecimal subtract = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(ss));
                    frozenReleaseLogs1.setPreSendAmount(subtract.toString());
                    frozenReleaseLogs1.setSendAmount(lvo.getAmount());
                    frozenReleaseLogs1.setProportion(proportion);
                    frozenReleaseLogs1.setSourceAmount(sourec.getAmount());
                    BigDecimal multiply1 = subtract.subtract(multiply);
                    frozenReleaseLogs1.setSufSendAmount(multiply1.toString());//釋放完剩余
                    frozenReleaseLogs1.setTargetAddress(sourec.getAddress());
                    frozenReleaseLogs1.setUpdateTime(dateTime);
                    frozenReleaseLogs1.setWalletId(sourec.getWalletId());
                    frozenReleaseLogs1.setDetailId(detailRule.getDetailId());



                    FrozenReleaseAffirm frozenReleaseAffirm = new FrozenReleaseAffirm();
                    frozenReleaseAffirm.setCreateTime(dateTime);
                    String detailId = UuidUtil.getUUID();
                    frozenReleaseAffirm.setDetailId(detailRule.getDetailId());
                    frozenReleaseAffirm.setWalletId(sourec.getWalletId());
                    frozenReleaseAffirm.setPayHash(paymentHash);
                    frozenReleaseAffirm.setLogId(logId);
                    service.payment(lvo, rule.getChianId());
                    for (FrozenReleaseLogFail frlf:frozenReleaseLogFails) {
                        int i = frozenReleaseLogFailMapper.deleteByPrimaryKey(frlf.getId());
                        logger.info("before中删除错误表记录："+i);
                    }
                    logger.info("释放日志：钱包ID：",frozenReleaseLogs1.getWalletId()+"转账金额："+frozenReleaseLogs1.getSendAmount()+"payCode:"+lvo.getResCode());
                    if (lvo.getResCode().equals(0)){
                        logger.debug("释放转账成功"+lvo.toString());
                        paymentHash = lvo.getPaymentHash();
                        frozenReleaseLogs1.setPayHash(paymentHash);
                        frozenReleaseAffirm.setPayHash(paymentHash);
                        Boolean aBoolean =false;
                        try {
                            aBoolean = insertData(frozenReleaseLogs1, frozenReleaseAffirm);
                        }catch (Exception e){
                            logger.error("释放日志入库失败"+paymentHash);
                            logger.debug("释放错误信息："+e);
                        }
                        if(aBoolean){
                            logger.debug("释放记录入库成功"+paymentHash);
                            frozenReleaseLogsin = null;
                            FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                            frozenReleaseExcelSource.setSourceId(sourec.getSourceId());
                            frozenReleaseExcelSource.setSourceStatus(1);
                            int i1 = 1;
                            if(sourec.getSourceStatus() == 0){
                                i1 = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
                            }                                if(i1 <=0){
                                logger.error("用户状态修改失败1"+sourec.getSourceId());
                            }
                            //修改规则正在执行状态
                            Integer frStatus = rule.getFrStatus();
                            if(frStatus == 0){
                                FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                                frozenReleaseRule.setRuleId(rule.getRuleId());
                                frozenReleaseRule.setFrStatus(1);
                                int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                                if(i <= 0){
                                    logger.error("大规则修改状态失败1"+rule.getRuleId());
                                }
                            }
                            Integer detailStatus = detailRule.getDetailStatus();
                            if(detailStatus == 0){
                                FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                                frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                                frozenReleaseDetailRule.setDetailStatus(1);
                                int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                                if(i <= 0){
                                    logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                                }
                            }
                        }else{
                            logger.error("释放日志入库失败hash"+paymentHash);
                        }
                       // releaseSuccess(frozenReleaseAffirm);
                        logger.info("错误数据释放+amount");
                    } else {
                        String message = lvo.getMessage();
                        logger.error("释放转账失败添加进错误日志");
                        frozenReleaseLogs1.setLogStatus(1);
                        frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
                        FrozenReleaseLogFail frozenReleaseLogsfyy = new FrozenReleaseLogFail();
                        frozenReleaseLogsfyy.setZid(UuidUtil.getUUID());
                        frozenReleaseLogsfyy.setCreateTime(DateUtils.getDateTime());
                        frozenReleaseLogsfyy.setLedgerAdress(frozenReleaseLogs1.getLedgerAdress());
                        frozenReleaseLogsfyy.setLogId(frozenReleaseLogs1.getLogId());
                        frozenReleaseLogsfyy.setFailState(2);
                        frozenReleaseLogsfyy.setSendAmount(frozenReleaseLogs1.getSendAmount());
                        frozenReleaseLogsfyy.setTargetAddress(frozenReleaseLogs1.getTargetAddress());
                        frozenReleaseLogsfyy.setUpdateTime(frozenReleaseLogs1.getUpdateTime());
                        frozenReleaseLogsfyy.setDetailId(frozenReleaseLogs1.getDetailId());
                        frozenReleaseLogFailMapper.insert(frozenReleaseLogsfyy);
                        FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                        frozenReleaseExcelSource.setSourceId(sourec.getSourceId());
                        frozenReleaseExcelSource.setSourceStatus(1);
                        int i1 = 1;
                        if(sourec.getSourceStatus() == 0){
                            i1 = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
                        }
                        if(i1 <=0){
                            logger.error("用户状态修改失败1"+sourec.getSourceId());
                        }
                        //修改规则正在执行状态
                        Integer frStatus = rule.getFrStatus();
                        if(frStatus == 0){
                            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                            frozenReleaseRule.setRuleId(rule.getRuleId());
                            frozenReleaseRule.setFrStatus(1);
                            int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                            if(i <= 0){
                                logger.error("大规则修改状态失败1"+rule.getRuleId());
                            }
                        }
                        Integer detailStatus = detailRule.getDetailStatus();
                        if(detailStatus == 0){
                            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                            frozenReleaseDetailRule.setDetailStatus(1);
                            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                            if(i <= 0){
                                logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                            }
                        }
                    }

                }
            }else{
                String walletId = sourec.getWalletId();
                //查询失败记录中中改规则下该钱包失败的记录
                List<FrozenReleaseLogFail> frozenReleaseLogFails = selectByWalletIdRuleId(walletId, rule.getRuleId());
                BigDecimal padAmount = BigDecimal.ZERO;
                if(frozenReleaseLogFails.size()>0){
                    for (FrozenReleaseLogFail ffail: frozenReleaseLogFails) {
                        padAmount =padAmount.add(new BigDecimal(ffail.getSendAmount()));
                    }
                }

                String amount = sourec.getAmount();
                Integer proportion = detailRule.getProportion();//应释放的%比
                Integer leftProportion = detailRule.getLeftProportion();//剩余的%比
                int nowProportion = detailRule.getLeftProportion() - proportion;//释放后剩余的%比
                BigDecimal bigDecimal = new BigDecimal(amount);
                BigDecimal multiply = bigDecimal.multiply(new BigDecimal(proportion)).divide(new BigDecimal(1000000));//应释放金额
                multiply= multiply.add(padAmount);//加上失败的金额
                BigDecimal subsecondamount = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(multiply.toString()));
                //调用转账接口
                ChainService service = getChainService(rule.getChianId());
                LPaymentVO lvo = new LPaymentVO();
                lvo.setAmount(multiply.toString());
                lvo.setCurrency(rule.getSubChain().getCurrency().toUpperCase());
                lvo.setDestAddress(sourec.getAddress());
                lvo.setFromAddress(rule.getPublicKey());
                lvo.setMemo("冻结资产释放");
                lvo.setPpwd(password);
                lvo.setCustid(rule.getRuleId());
                lvo.setSecretkey(rule.getPayPasswd());
                lvo.setUuid(UuidUtil.getUUID());
                if(rule.getSubChain() != null){
                    if(rule.getSubChain().getBaseChain() == null){
                        if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                            //设置银冠
                            lvo.setIssuer(rule.getSubChain().getTokenAddress());
                        }
                    }else {

                        if( rule.getSubChain().getBaseChain() !=true){
                            if(!StringUtils.isEmpty(rule.getSubChain().getTokenAddress())){
                                //设置银冠
                                lvo.setIssuer(rule.getSubChain().getTokenAddress());
                            }
                        }
                    }
                }
                String paymentHash = lvo.getPaymentHash();
                //封装数据
                FrozenReleaseLogs frozenReleaseLogs1 = new FrozenReleaseLogs();
                //frozenReleaseLogs1.setConfirmTime();
                String dateTime = DateUtils.getDateTime();
                frozenReleaseLogs1.setCreateTime(dateTime);
                frozenReleaseLogs1.setLedgerAdress(rule.getPublicKey());
                String logId = UuidUtil.getUUID();
                frozenReleaseLogs1.setLogId(logId);
                frozenReleaseLogs1.setLogStatus(2);
                frozenReleaseLogs1.setPadAmount(padAmount.toString());//上次失败填充的钱数
                frozenReleaseLogs1.setPayHash(paymentHash);
                frozenReleaseLogs1.setPayTime(dateTime);
                String ss = selectReleaseMoney(sourec.getWalletId(), sourec.getRuleId());
                BigDecimal subtract = new BigDecimal(sourec.getAmount()).subtract(new BigDecimal(ss));
                frozenReleaseLogs1.setPreSendAmount(subtract.toString());
                frozenReleaseLogs1.setSendAmount(lvo.getAmount());
                frozenReleaseLogs1.setProportion(proportion);
                frozenReleaseLogs1.setSourceAmount(sourec.getAmount());
                BigDecimal multiply1 = subtract.subtract(multiply);
                frozenReleaseLogs1.setSufSendAmount(multiply1.toString());//釋放完剩余
                frozenReleaseLogs1.setTargetAddress(sourec.getAddress());
                frozenReleaseLogs1.setUpdateTime(dateTime);
                frozenReleaseLogs1.setWalletId(sourec.getWalletId());
                frozenReleaseLogs1.setDetailId(detailRule.getDetailId());



                FrozenReleaseAffirm frozenReleaseAffirm = new FrozenReleaseAffirm();
                frozenReleaseAffirm.setCreateTime(dateTime);
                String detailId = UuidUtil.getUUID();
                frozenReleaseAffirm.setDetailId(detailRule.getDetailId());
                frozenReleaseAffirm.setWalletId(sourec.getWalletId());
                frozenReleaseAffirm.setPayHash(paymentHash);
                frozenReleaseAffirm.setLogId(logId);
                service.payment(lvo, rule.getChianId());
                for (FrozenReleaseLogFail frlf:frozenReleaseLogFails) {
                    //  frozenReleaseLogFailMapper.deleteByPrimaryKey(frlf.getId());
                    int i = frozenReleaseLogFailMapper.deleteByPrimaryKey(frlf.getId());
                    logger.info("before中删除错误表记录799："+i);
                }
                if (lvo.getResCode().equals(0)){
                    logger.debug("释放转账成功"+lvo.toString());
                    paymentHash = lvo.getPaymentHash();
                    frozenReleaseLogs1.setPayHash(paymentHash);
                    frozenReleaseAffirm.setPayHash(paymentHash);
                    Boolean aBoolean =false;
                    try {
                        aBoolean = insertData(frozenReleaseLogs1, frozenReleaseAffirm);
                        logger.debug("正确时时释放日志入库885");
                    }catch (Exception e){
                        logger.error("释放日志入库失败"+paymentHash);
                        logger.debug("释放错误信息："+e);
                    }
                    if(aBoolean){
                        logger.debug("释放记录入库成功"+paymentHash);
                        frozenReleaseLogsin = null;
                        FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                        frozenReleaseExcelSource.setSourceId(sourec.getSourceId());
                        frozenReleaseExcelSource.setSourceStatus(1);
                        int i1 = 1;
                        if(sourec.getSourceStatus() == 0){
                            i1 = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
                        }                        if(i1 <=0){
                            logger.error("用户状态修改失败1"+sourec.getSourceId());
                        }
                        //修改规则正在执行状态
                        Integer frStatus = rule.getFrStatus();
                        if(frStatus == 0){
                            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                            frozenReleaseRule.setRuleId(rule.getRuleId());
                            frozenReleaseRule.setFrStatus(1);
                            int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                            if(i <= 0){
                                logger.error("大规则修改状态失败1"+rule.getRuleId());
                            }
                        }
                        Integer detailStatus = detailRule.getDetailStatus();
                        if(detailStatus == 0){
                            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                            frozenReleaseDetailRule.setDetailStatus(1);
                            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                            if(i <= 0){
                                logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                            }
                        }
                    }else{
                        logger.error("释放日志入库失败hash"+paymentHash);
                    }
                   // releaseSuccess(frozenReleaseAffirm);
                } else {
                    frozenReleaseLogs1.setLogStatus(1);
                    int insert = frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
                    logger.debug("错误时释放日志入库928："+insert);
                    String message = lvo.getMessage();
                    logger.error("释放转账失败添加进错误日志");
                    FrozenReleaseLogFail frozenReleaseLogsfyy = new FrozenReleaseLogFail();
                    frozenReleaseLogsfyy.setZid(UuidUtil.getUUID());
                    frozenReleaseLogsfyy.setCreateTime(DateUtils.getDateTime());
                    frozenReleaseLogsfyy.setLedgerAdress(frozenReleaseLogs1.getLedgerAdress());
                    frozenReleaseLogsfyy.setLogId(frozenReleaseLogs1.getLogId());
                    frozenReleaseLogsfyy.setSendAmount(frozenReleaseLogs1.getSendAmount());
                    frozenReleaseLogsfyy.setFailState(2);
                    frozenReleaseLogsfyy.setTargetAddress(frozenReleaseLogs1.getTargetAddress());
                    frozenReleaseLogsfyy.setUpdateTime(frozenReleaseLogs1.getUpdateTime());
                    frozenReleaseLogsfyy.setDetailId(frozenReleaseLogs1.getDetailId());
                    frozenReleaseLogFailMapper.insert(frozenReleaseLogsfyy);
                    FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                    frozenReleaseExcelSource.setSourceId(sourec.getSourceId());
                    frozenReleaseExcelSource.setSourceStatus(1);
                    int i1 = 1;
                    if(sourec.getSourceStatus() == 0){
                        i1 = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);                   }                    if(i1 <=0){
                        logger.error("用户状态修改失败1"+sourec.getSourceId());
                    }
                    //修改规则正在执行状态
                    Integer frStatus = rule.getFrStatus();
                    if(frStatus == 0){
                        FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                        frozenReleaseRule.setRuleId(rule.getRuleId());
                        frozenReleaseRule.setFrStatus(1);
                        int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                        if(i <= 0){
                            logger.error("大规则修改状态失败1"+rule.getRuleId());
                        }
                    }
                    Integer detailStatus = detailRule.getDetailStatus();
                    if(detailStatus == 0){
                        FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                        frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
                        frozenReleaseDetailRule.setDetailStatus(1);
                        int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
                        if(i <= 0){
                            logger.error("小规则修改状态失败1"+detailRule.getDetailId());
                        }
                    }
                }
            }
            }
        //规则执行完成
        FrozenReleaseDetailRule frozenReleaseDetailRule2 = frozenReleaseDetailRuleMapper.selecByDetailId(detailRule.getDetailId());
        Integer detailStatus = frozenReleaseDetailRule2.getDetailStatus();
        if(detailStatus == 1){
            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
            frozenReleaseDetailRule.setDetailId(detailRule.getDetailId());
            frozenReleaseDetailRule.setDetailStatus(2);
            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(frozenReleaseDetailRule);
            if(i <= 0){
                logger.debug("小规则状态修改失败2");
                return false;
            }
        }
        FrozenReleaseRule frozenReleaseRule1 = frozenReleaseRuleMapper.selectByRuleId(rule.getRuleId());
        Integer frStatus = frozenReleaseRule1.getFrStatus();
        //查询detailrule
        FrozenReleaseDetailRule frozenReleaseDetailRule1 = frozenReleaseDetailRuleMapper.selecByDetailId(detailRule.getDetailId());
        if(frStatus == 1 && frozenReleaseDetailRule1.getLeftProportion() == 0){
            FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
            frozenReleaseRule.setRuleId(rule.getRuleId());
            frozenReleaseRule.setFrStatus(2);
            int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
            if(i <= 0){
                logger.error("大规则修改状态失败2");
                return false;
            }
        }else {
            //添加匀速详细规则
            if(rule.getFrType() == 0){
                FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
                String dateTime = DateUtils.getDateTime();
                frozenReleaseDetailRule.setAvgDays(detailRule.getAvgDays());
                frozenReleaseDetailRule.setCreateTime(dateTime);
                frozenReleaseDetailRule.setDetailId(UuidUtil.getUUID());
                frozenReleaseDetailRule.setDetailStatus(0);
                frozenReleaseDetailRule.setAppId(detailRule.getAppId());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String executTime = detailRule.getExecutTime();
                Date date = null;
                try {
                    date=simpleDateFormat.parse(executTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date1 = DateUtils.addDays(date, detailRule.getAvgDays());
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date1);
                frozenReleaseDetailRule.setExecutTime(dateStr);
                if(detailRule.getLeftProportion() < detailRule.getProportion()){
                    frozenReleaseDetailRule.setProportion(detailRule.getLeftProportion());
                    frozenReleaseDetailRule.setLeftProportion(0);
                }else{
                    frozenReleaseDetailRule.setProportion(detailRule.getProportion());
                    frozenReleaseDetailRule.setLeftProportion(detailRule.getLeftProportion() - detailRule.getProportion());
                }
                frozenReleaseDetailRule.setRuleId(detailRule.getRuleId());
                frozenReleaseDetailRule.setUpdateTime(dateTime);
                int insert = frozenReleaseDetailRuleMapper.insert(frozenReleaseDetailRule);
                if(insert < 0){
                    logger.error("匀速增加detail规则失败"+detailRule.getDetailId());
                }
                if(date1.before(new Date())){
                    FrozenReleaseDetailRule releaseRuleone = frozenReleaseDetailRuleMapper.selecByDetailId(frozenReleaseDetailRule.getDetailId());
                    detailRelease(releaseRuleone,null,"releaseAmount");
                }
            }
        }
        return true;
    }

    public void releaseSuccess(FrozenReleaseAffirm fraf) {
        FrozenReleaseLogs frozenReleaseLogs2 = frozenReleaseLogsMapper.selectByLogId(fraf.getLogId());
        FrozenReleaseLogs frozenReleaseLogs = new FrozenReleaseLogs();
        frozenReleaseLogs.setId(frozenReleaseLogs2.getId());
        frozenReleaseLogs.setLogId(fraf.getLogId());
        String dateTime = DateUtils.getDateTime();
        frozenReleaseLogs.setConfirmTime(dateTime);
        frozenReleaseLogs.setLogStatus(0);
        frozenReleaseLogs.setUpdateTime(dateTime);
        int i = frozenReleaseLogsMapper.updateByPrimaryKeySelective(frozenReleaseLogs);
        FrozenReleaseDetailRule frozenReleaseDetailRule = frozenReleaseDetailRuleMapper.selecByDetailId(frozenReleaseLogs2.getDetailId());
        FrozenReleaseExcelSource frozenReleaseExcelSourcefy =frozenReleaseExcelSourceMapper.selectSourceByWalletIdRuleId(frozenReleaseLogs2.getWalletId(),frozenReleaseDetailRule.getRuleId());
        FrozenReleaseExcelSourceAmount frozenReleaseExcelSourceReleaseAmount = frozenReleaseExcelSourceReleaseAmountMapper.selectBySourceId(frozenReleaseExcelSourcefy.getSourceId());
        int i1 = frozenReleaseAffirmMapper.deleteByPrimaryKey(fraf.getId());
        if(i1 <=0){
            logger.error("日志支付确认表删除失败："+fraf.getId()+"111");
        }else{
            if(frozenReleaseExcelSourceReleaseAmount != null){
               // String updateTime = frozenReleaseExcelSourcefy.getUpdateTime();
               // FrozenReleaseExcelSource frozenReleaseExcelSource = frozenReleaseExcelSourceMapper.selectByPrimaryKey(frozenReleaseExcelSourceReleaseAmount.getSourceId());
                BigDecimal leftAmount = new BigDecimal(frozenReleaseExcelSourceReleaseAmount.getAmount());
                BigDecimal secondAmount = new BigDecimal(frozenReleaseLogs2.getSendAmount());
                BigDecimal subtract = leftAmount.subtract(secondAmount);
                FrozenReleaseExcelSourceAmount frozenReleaseExcelSourceReleaseAmount1 = new FrozenReleaseExcelSourceAmount();
                frozenReleaseExcelSourceReleaseAmount1.setSourceId(frozenReleaseExcelSourceReleaseAmount.getSourceId());
                frozenReleaseExcelSourceReleaseAmount1.setAmount(subtract.toString());
                Integer s = frozenReleaseExcelSourceReleaseAmountMapper.updateByprivateAndupdateTime(subtract.toString(), DateUtils.getDateTime(), frozenReleaseExcelSourceReleaseAmount.getId());
            }
        }
        FrozenReleaseLogs frozenReleaseLogs1 = frozenReleaseLogsMapper.selectByLogId(fraf.getLogId());
        String sufSendAmount = frozenReleaseLogs1.getSufSendAmount();
        if(sufSendAmount.equals("0")){
            int update = updateStateExcelSource(frozenReleaseLogs1, 2);
        }
    }

     int  updateStateExcelSource(FrozenReleaseLogs frozenReleaseLogs1,Integer i){
         FrozenReleaseExcelSource sour =  frozenReleaseExcelSourceMapper.selectByWalletIdDetailId(frozenReleaseLogs1.getLogId());
         FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
         frozenReleaseExcelSource.setSourceId(sour.getSourceId());
         frozenReleaseExcelSource.setSourceStatus(i);
         int update = frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
         return update;
     }
}
