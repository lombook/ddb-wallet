package com.jinglitong.wallet.job.service;


import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.ddbapi.model.FrozenReleaseMiddlePay;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.*;
import com.jinglitong.wallet.job.util.DateUtils;
import com.jinglitong.wallet.job.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReleaseService {

    @Autowired
    private FrozenReleaseMiddlePayMapper frozenReleaseMiddlePayMapper;


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

    @Autowired
    private TransactionalService transactionalService;

    @Value("${beforeTime}")
    private long  timebefore;

    private Boolean chekcFlag = true;



    //释放
    public Boolean releaseAmount() {
        while (true){
            List<FrozenReleaseMiddlePay> middlePayList = frozenReleaseMiddlePayMapper.selectByState();
            if(middlePayList != null && middlePayList.size() != 0){
                for (FrozenReleaseMiddlePay middle : middlePayList) {
                    frozenReleaseMiddlePayMapper.updateStateByZid(middle.getSourceId(),1,middle.getDetailId());
                    //转账
                    transactionalService.transferReleasAmount(middle);
                }
            }else {
                break;
            }
        }

        return true;
    }




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
        if(affirmlist.size()>0){
            chekcAffirem(affirmlist);
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



    private ChainService getChainService(String chainId) {
        MainChain mainChain = mainChainMapper.selectByChainId(chainId);
        if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) ){
            return null;
        }
        ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
        return chainService;

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
        if(frozenReleaseDetailRule.getDetailStatus() == 2){
            FrozenReleaseDetailRule frozenReleaseDetailRule2 = new FrozenReleaseDetailRule();
            frozenReleaseDetailRule2.setId(frozenReleaseDetailRule.getId());
            frozenReleaseDetailRule2.setDetailStatus(3);
            frozenReleaseDetailRuleMapper.updateByPrimaryKeySelective(frozenReleaseDetailRule2);
        }
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
        int reusult = new BigDecimal(sufSendAmount).compareTo(BigDecimal.ZERO);
        if(sufSendAmount.equals("0") || sufSendAmount.equals("0.0") || reusult == 0){
            int update = updateStateExcelSource(frozenReleaseLogs1, 2);
            FrozenReleaseDetailRule frozenReleaseDetailRule1 = frozenReleaseDetailRuleMapper.selecByDetailId(frozenReleaseLogs1.getDetailId());
            if(frozenReleaseDetailRule1.getLeftProportion() == 0){
                FrozenReleaseRule frozenReleaseRule = frozenReleaseRuleMapper.selectByRuleId(frozenReleaseDetailRule1.getRuleId());
                FrozenReleaseRule frozenReleaseRule1 = new FrozenReleaseRule();
                frozenReleaseRule1.setId(frozenReleaseRule.getId());
                frozenReleaseRule1.setFrStatus(2);
                frozenReleaseRuleMapper.updateByPrimaryKeySelective(frozenReleaseRule1);
            }
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
