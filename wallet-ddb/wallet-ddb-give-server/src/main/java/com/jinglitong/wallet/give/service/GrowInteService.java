package com.jinglitong.wallet.give.service;

import com.jinglitong.wallet.ddbapi.model.DdbGrowIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbShouldFrozen;
import com.jinglitong.wallet.give.mapper.DdbGrowIntegralRecordMapper;
import com.jinglitong.wallet.give.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.give.mapper.DdbShouldFrozenMapper;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional
@Slf4j
@Service
public class GrowInteService {

    @Autowired
    private DdbShouldFrozenMapper ddbShouldFrozenMapper;

    @Autowired
    private DdbIntegralAccountService ddbIntegralAccountService;

    @Autowired
    private DdbIntegralAccountMapper ddbIntegralAccountMapper;

    @Autowired
    private DdbGrowIntegralRecordMapper ddbGrowIntegralRecordMapper;
    /**
     * 处理增长
     * @param ddb
     */
    public void growIntege(DdbShouldFrozen ddb) {
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date excuteTime=sDateFormat.parse(ddb.getExcuteTime());
            Date createTime=sDateFormat.parse(ddb.getCreateTime());
            Long createDays  = DateUtils.pastDays(createTime);//创建时间到现在一共几天
            Long todaySumAmount = createDays * ddb.getRfrozenAmount();
           // ddbShouldFrozenMapper.updateTodayFrozenAmountByzid(todaySumAmount,ddb.getZid());
            //转账
            frozenTransfer(excuteTime,ddb);
        }catch (Exception e){
            log.info("日期装换失败"+e);
        }
    }


    @Transactional
    public  void frozenTransfer(Date excuteTime,DdbShouldFrozen ddb) {
        if(ddb.getLeftAmount()<ddb.getRfrozenAmount()){
            int amount = ddb.getLeftAmount();
            log.info("最后一次释放amount="+ddb.getLeftAmount());
            DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(ddb.getRealName());
            ddbIntegralAccountService.addInteg(amount,ddb.getCustId(),ddb.getFlowId(),ddbIntegralAccount,"释放总账往积分转账");
            String dateTime = DateUtils.formatDateTime(DateUtils.addDays(excuteTime, 1));
            ddbShouldFrozenMapper.updateFrozenDaysAndleftAmounttoZeroByshouId(dateTime,ddb.getZid());
            createDdbGrowRecord(ddb,amount);
        }else {
            DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(ddb.getRealName());
            ddbIntegralAccountService.addInteg(ddb.getRfrozenAmount(),ddb.getCustId(),ddb.getFlowId(),ddbIntegralAccount,"释放总账往积分转账");
            String dateTime = DateUtils.formatDateTime(DateUtils.addDays(excuteTime, 1));
            // DdbShouldFrozen ddbShouldFrozen = ddbShouldFrozenMapper.selectByShouldTransferId(ddb.getShouldTransferId());
            //  log.info(test++ +"       每次释放leftAmount={},amount={},tofreeAmount={}，shengyu={}",ddbShouldFrozen.getLeftAmount(),ddbShouldFrozen.getAmount(),ddbShouldFrozen.getRfrozenAmount(),ddbShouldFrozen.getLeftAmount()-ddbShouldFrozen.getRfrozenAmount());
            ddbShouldFrozenMapper.updateFrozenDaysAndleftAmountByshouId(dateTime,ddb.getZid());
            createDdbGrowRecord(ddb,ddb.getRfrozenAmount());
        }
    }

    private void createDdbGrowRecord(DdbShouldFrozen ddb, int amount) {
        DdbGrowIntegralRecord ddbGrowIntegralRecord = new DdbGrowIntegralRecord();
        ddbGrowIntegralRecord.setAmount(amount);
        ddbGrowIntegralRecord.setCreateTime(DateUtils.getDateTime());
        ddbGrowIntegralRecord.setCustId(ddb.getCustId());
        ddbGrowIntegralRecord.setFlowId(ddb.getFlowId());
        ddbGrowIntegralRecord.setGruleId(ddb.getRuleId());
        ddbGrowIntegralRecord.setIntegCname(ddb.getInteCname());
        ddbGrowIntegralRecord.setIntegName(ddb.getInteName());
        ddbGrowIntegralRecord.setInteZid(ddb.getInteZid());
        ddbGrowIntegralRecord.setLeftAmount(ddb.getLeftAmount() - ddb.getRfrozenAmount());
        ddbGrowIntegralRecord.setProportion(ddb.getProportion());
        ddbGrowIntegralRecord.setRealCname(ddb.getRealCname());
        ddbGrowIntegralRecord.setRealName(ddb.getRealName());
        ddbGrowIntegralRecord.setRemark("增长释放");
        ddbGrowIntegralRecord.setSumAmount(ddb.getAmount());
        ddbGrowIntegralRecord.setZid(UuidUtil.getUUID());
        ddbGrowIntegralRecord.setRuleId(ddb.getRuleId());
        ddbGrowIntegralRecord.setFrozenDays(ddb.getFrozenDays()+1);
        ddbGrowIntegralRecordMapper.insert(ddbGrowIntegralRecord);
    }


}
