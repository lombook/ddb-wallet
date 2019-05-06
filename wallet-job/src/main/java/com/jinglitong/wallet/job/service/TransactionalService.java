package com.jinglitong.wallet.job.service;


import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.ddbapi.model.FrozenReleaseMiddlePay;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.*;
import com.jinglitong.wallet.job.util.DateUtils;
import com.jinglitong.wallet.job.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class TransactionalService {

    @Autowired
    private FrozenReleaseDetailRuleMapper frozenReleaseDetailRuleMapper;

    @Autowired
    private FrozenReleaseMiddlePayMapper frozenReleaseMiddlePayMapper;

    @Autowired
    private FrozenReleaseRuleMapper frozenReleaseRuleMapper;

    @Autowired
    private FrozenReleaseExcelSourceMapper frozenReleaseExcelSourceMapper;

    @Autowired
    private FrozenReleaseLogsMapper frozenReleaseLogsMapper ;

    @Autowired
    private  MainChainMapper mainChainMapper;

    @Autowired
    private FrozenReleaseAffirmMapper frozenReleaseAffirmMapper;

    @Autowired
    private FrozenReleaseLogFailMapper frozenReleaseLogFailMapper;

    public Boolean saveMiddlePay(FrozenReleaseDetailRule detailRule) {
        String dateTime = DateUtils.getDateTime();
        List<FrozenReleaseMiddlePay> releaseList = new ArrayList<>();
        FrozenReleaseRule rule = detailRule.getRule();
        List<FrozenReleaseExcelSource> sourceList = frozenReleaseExcelSourceMapper.selectSourceRuleIdNotState(rule.getRuleId(),3);
        for (FrozenReleaseExcelSource excelResourc:sourceList) {
           FrozenReleaseMiddlePay middlePay = frozenReleaseMiddlePayMapper.selectMiddleByDetailIdAndSourceId(detailRule.getDetailId(),excelResourc.getSourceId());
           if(middlePay == null){
               Integer proportion = detailRule.getProportion();//应释放的%比
               BigDecimal bigDecimal = new BigDecimal(excelResourc.getAmount());
               BigDecimal amount = bigDecimal.multiply(new BigDecimal(proportion)).divide(new BigDecimal(1000000));//应释放金额
               FrozenReleaseMiddlePay frozenReleaseMiddlePay = new FrozenReleaseMiddlePay();
               frozenReleaseMiddlePay.setZid(UuidUtil.getUUID());
               Integer leftProportion = detailRule.getLeftProportion();//剩余的%比
               Integer nowProportion = detailRule.getLeftProportion() - proportion;//释放后剩余的%比
               String ss = selectReleaseMoney(excelResourc.getWalletId(), excelResourc.getRuleId());
               BigDecimal preAmount = new BigDecimal(excelResourc.getAmount()).subtract(new BigDecimal(ss));
               frozenReleaseMiddlePay.setPreSendAmount(preAmount.toString());
               frozenReleaseMiddlePay.setSourceAmount(excelResourc.getAmount());
               BigDecimal sufAmount = preAmount.subtract(amount);
               frozenReleaseMiddlePay.setSufSendAmount(sufAmount.toString());//釋放完剩余
               frozenReleaseMiddlePay.setAmount(amount.toString());
               frozenReleaseMiddlePay.setChainId(rule.getChianId());
               frozenReleaseMiddlePay.setCoinName(rule.getSubChain().getCurrency());
               frozenReleaseMiddlePay.setCreateTime(dateTime);
               frozenReleaseMiddlePay.setDetailId(detailRule.getDetailId());
               frozenReleaseMiddlePay.setExecutTime(detailRule.getExecutTime());
               frozenReleaseMiddlePay.setFromAddress(rule.getPublicKey());
               frozenReleaseMiddlePay.setMemos("冻结预生成数据");
               frozenReleaseMiddlePay.setSourceId(excelResourc.getSourceId());
               frozenReleaseMiddlePay.setToAddress(excelResourc.getAddress());
               frozenReleaseMiddlePay.setWalletId(excelResourc.getWalletId());
               frozenReleaseMiddlePay.setStatus(0);
               frozenReleaseMiddlePay.setTokenAddress(rule.getSubChain().getTokenAddress());
               frozenReleaseMiddlePay.setProportion(proportion);
               frozenReleaseMiddlePay.setRuleId(rule.getRuleId());
               frozenReleaseMiddlePay.setUpdateTime(dateTime);
               releaseList.add(frozenReleaseMiddlePay);
               if(excelResourc.getSourceStatus() == 0){
                   FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
                   frozenReleaseExcelSource.setSourceId(excelResourc.getSourceId());
                   frozenReleaseExcelSource.setSourceStatus(1);
                   frozenReleaseExcelSourceMapper.updateBySourdeIdSelective(frozenReleaseExcelSource);
               }
           }
        }
        if(releaseList.size()> 0){
            //批量插入
            frozenReleaseMiddlePayMapper.insertList(releaseList);
        }

        //添加匀速详细规则
        if(rule.getFrType() == 0 && detailRule.getLeftProportion() != 0  && detailRule.getDetailStatus() != 1){
            FrozenReleaseDetailRule frozenReleaseDetailRule = new FrozenReleaseDetailRule();
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
                log.error("预生成匀速股则，日期转换失败"+e.toString());
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
                log.error("匀速增加detail规则失败"+detailRule.getDetailId());
                return false;
            }
        }
        Integer detailStatus = detailRule.getDetailStatus();
        if(detailStatus == 0 || detailStatus == 1){
            FrozenReleaseDetailRule detailRuleUpdate = new FrozenReleaseDetailRule();
            detailRuleUpdate.setDetailId(detailRule.getDetailId());
            detailRuleUpdate.setDetailStatus(2);
            int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(detailRuleUpdate);
            if(i <= 0){
                log.error("小规则修改状态失败2"+detailRule.getDetailId());
            }
        }
        return  true;
    }



    //查询指定规则指定钱包释放成功总金额
    private String selectReleaseMoney(String walletId, String ruleId){
        BigDecimal amount = new BigDecimal("0");
       // List<FrozenReleaseLogs> list = frozenReleaseLogsMapper.selectByWRid(walletId,ruleId);
        List<FrozenReleaseMiddlePay> middlePayList = frozenReleaseMiddlePayMapper.selectByWrid(walletId,ruleId);
        if(middlePayList.size() > 0){
            for (FrozenReleaseMiddlePay f:middlePayList) {
                amount = amount.add(new BigDecimal(f.getAmount()));
            }
        }
        return amount.toString();
    }

        private ChainService getChainService(String chainId) {
            MainChain mainChain = mainChainMapper.selectByChainId(chainId);
            if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) ){
                return null;
            }
            ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
            return chainService;

        }

    //保存日志和确认表
    private Boolean insertData(FrozenReleaseLogs frozenReleaseLogs1, FrozenReleaseAffirm frozenReleaseAffirm) throws Exception{
        int insert = frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
        int insert1 = frozenReleaseAffirmMapper.insert(frozenReleaseAffirm);
        if(insert == 1 && insert1 == 1){
            return true;
        } else{
            return false;
        }
    }


    public void transferReleasAmount(FrozenReleaseMiddlePay middle) {
        FrozenReleaseExcelSource soso = frozenReleaseExcelSourceMapper.selectBySourceId(middle.getSourceId());
        if (soso.getSourceStatus() == 3) {
            frozenReleaseMiddlePayMapper.updateStateByZid(middle.getSourceId(), 3, middle.getDetailId());
            return;
        } else {
            ChainService service = getChainService(middle.getChainId());
            LPaymentVO lvo = new LPaymentVO();
            lvo.setAmount(middle.getAmount());
            lvo.setCurrency(middle.getCoinName());
            lvo.setDestAddress(middle.getToAddress());
            lvo.setFromAddress(middle.getFromAddress());
            lvo.setMemo("冻结资产释放");
            lvo.setPpwd("123456");
            lvo.setCustid(middle.getRuleId());
            FrozenReleaseRule frozenRule = frozenReleaseRuleMapper.selectByRuleId(middle.getRuleId());
            lvo.setSecretkey(frozenRule.getPayPasswd());
            lvo.setUuid(UuidUtil.getUUID());
            if (middle.getTokenAddress() != null && !"".equals(middle.getTokenAddress())) {
                lvo.setIssuer(middle.getTokenAddress());
            } else {
                lvo.setIssuer("");
            }

            String paymentHash = lvo.getPaymentHash();
            //封装数据
            FrozenReleaseLogs frozenReleaseLogs1 = new FrozenReleaseLogs();
            //frozenReleaseLogs1.setConfirmTime();
            String dateTime = DateUtils.getDateTime();
            frozenReleaseLogs1.setCreateTime(dateTime);
            frozenReleaseLogs1.setLedgerAdress(middle.getFromAddress());
            String logId = UuidUtil.getUUID();
            frozenReleaseLogs1.setLogId(logId);
            frozenReleaseLogs1.setLogStatus(2);
            frozenReleaseLogs1.setPadAmount("");//上次失败填充的钱数
            frozenReleaseLogs1.setPayHash(paymentHash);
            frozenReleaseLogs1.setPayTime(dateTime);
            frozenReleaseLogs1.setPreSendAmount(middle.getPreSendAmount());
            frozenReleaseLogs1.setSendAmount(lvo.getAmount());
            frozenReleaseLogs1.setSourceAmount(middle.getSourceAmount());
            frozenReleaseLogs1.setSufSendAmount(middle.getSufSendAmount());//釋放完剩余
            frozenReleaseLogs1.setTargetAddress(middle.getToAddress());
            frozenReleaseLogs1.setUpdateTime(dateTime);
            frozenReleaseLogs1.setWalletId(middle.getWalletId());
            frozenReleaseLogs1.setDetailId(middle.getDetailId());
            frozenReleaseLogs1.setProportion(middle.getProportion());
            FrozenReleaseAffirm frozenReleaseAffirm = new FrozenReleaseAffirm();
            frozenReleaseAffirm.setCreateTime(dateTime);
            frozenReleaseAffirm.setDetailId(middle.getDetailId());
            frozenReleaseAffirm.setWalletId(middle.getWalletId());
            frozenReleaseAffirm.setPayHash(paymentHash);
            frozenReleaseAffirm.setLogId(logId);
            service.payment(lvo, middle.getChainId());
            log.info("释放日志：钱包ID："+ frozenReleaseLogs1.getWalletId() + "转账金额：" + frozenReleaseLogs1.getSendAmount() + "payCode:" + lvo.getResCode());

            if (lvo.getResCode().equals(0)) {
                log.debug("释放转账成功" + lvo.toString());
                paymentHash = lvo.getPaymentHash();
                frozenReleaseLogs1.setPayHash(paymentHash);
                frozenReleaseAffirm.setPayHash(paymentHash);
                Boolean aBoolean = false;
                try {
                    aBoolean = insertData(frozenReleaseLogs1, frozenReleaseAffirm);
                    updateMiddlePayState(middle,2,lvo.getPaymentHash());
                } catch (Exception e) {
                    log.error("释放日志入库失败" + paymentHash);
                    log.debug("释放错误信息：" + e);
                }

            } else {
                frozenReleaseLogs1.setLogStatus(1);
                frozenReleaseLogsMapper.insert(frozenReleaseLogs1);
                String message = lvo.getMessage();
                log.error("释放转账失败添加进错误日志");
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
                updateMiddlePayState(middle,4,lvo.getPaymentHash());
            }

        }
    }

    /**
     * 修改middelpay的状态
     * @param middle
     * @param state
     * @param payMentHash
     */
    private void updateMiddlePayState(FrozenReleaseMiddlePay middle,int state,String payMentHash){
        FrozenReleaseMiddlePay middlePay = new FrozenReleaseMiddlePay();
        middlePay.setId(middle.getId());
        middlePay.setStatus(state);
        middlePay.setTradeHash(payMentHash== null ? "":payMentHash);
        frozenReleaseMiddlePayMapper.updateByPrimaryKeySelective(middlePay);
    }
}
