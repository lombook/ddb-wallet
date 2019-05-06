package com.jinglitong.wallet.ddbserver.service;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.view.AppNoticeSubVo;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRuleVo;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWithdrawRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWithdrawRuleMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class DdbWithDrowStatusService {

    @Value("${zz.xh.realName}")
    private String xhAccount;

    @Autowired
    private DdbIntegralWithdrawRecordMapper ddbIntegralWithdrawRecordMapper;

    @Autowired
    private IntegerExchangeService integerExchangeService;

    @Autowired
    private DdbIntegralAccountMapper ddbIntegralAccountMapper;

    @Autowired
    private DdbIntegralWithdrawRuleMapper ddbIntegralWithdrawRuleMapper;

    @Autowired
    private DdbIntegralAccountService ddbIntegralAccountService;

    @Autowired
    private AppNoiceService appNoiceService;

    @Autowired
    private CustomerMapper customerMapper;

    //审核发送消息
    private String shNotPassTitle = "提现申请失败";
    private String shNotPassSubTitle = "审核不通过";
    private String shNotPassBody = "银行账户问题无法转账";
    //提现失败发送消息
    private String txFailTitle = "提现申请失败";
    private String txFailSubTitle = "提现失败";
    private String txFailBody = "银行账户问题无法转账";
    //提现成功发送消息
    private String txSuccessTitle = "提现申请成功";
    private String txSuccessSubTitle = "提现成功";
    private String txSuccessBody = "请关注银行卡动态";
    /**
     * 申请通过
     * @param withdrawRecord
     * @param admin
     * @return
     */
    public Map<String,String> doPass(DdbIntegralWithdrawRecord withdrawRecord, Admin admin) {
        HashMap<String, String> resultMap = new HashMap<>();
        //修改withdrownRecord的状态、操作人、修改时间
        DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord = new DdbIntegralWithdrawRecord();
        ddbIntegralWithdrawRecord.setZid(withdrawRecord.getZid());
        ddbIntegralWithdrawRecord = ddbIntegralWithdrawRecordMapper.selectOne(ddbIntegralWithdrawRecord);
        if(ddbIntegralWithdrawRecord.getApplyStatus() == 1){
            resultMap.put("status","fail");
            return resultMap;
        }
        Integer update = updateWithDrawRecord(ddbIntegralWithdrawRecord,1,0,"",admin,1);
        if(update >0){
            resultMap.put("status","success");
        }else {
            resultMap.put("status","fail");
        }
        return resultMap;
    }

    /**
     * 申请不通过
     * @param withdrawRecord
     * @param admin
     * @return
     */
    public Map<String,String> doNotPass(DdbIntegralWithdrawRecord withdrawRecord, Admin admin) {
        HashMap<String, String> resultMap = new HashMap<>();
        //修改withdrownRecord的状态、操作人、修改时间
        DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord = new DdbIntegralWithdrawRecord();
        ddbIntegralWithdrawRecord.setZid(withdrawRecord.getZid());
        ddbIntegralWithdrawRecord = ddbIntegralWithdrawRecordMapper.selectOne(ddbIntegralWithdrawRecord);
        DdbIntegralWithdrawRuleVo rule = ddbIntegralWithdrawRuleMapper.getRuleByZid(ddbIntegralWithdrawRecord.getWithdrawRuleId());
        DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(rule.getResaveIntegRealName());
        Customer customer = customerMapper.selectByCustId(ddbIntegralWithdrawRecord.getCustId());
        if(withdrawRecord.getRemark() == null || "".equals(withdrawRecord.getRemark())){
            withdrawRecord.setRemark(shNotPassBody);
        }
        if(ddbIntegralWithdrawRecord.getApplyStatus() == 2){
            resultMap.put("status","fail");
            return resultMap;
        }
        Integer update = updateWithDrawRecord(ddbIntegralWithdrawRecord,2,2,withdrawRecord.getRemark(),admin,1);
        integerExchangeService.addInteg(ddbIntegralWithdrawRecord.getAmount(),0-ddbIntegralWithdrawRecord.getAmount(),ddbIntegralWithdrawRecord.getCustId(),
                ddbIntegralWithdrawRecord.getZid(),ddbIntegralAccount,"提现申请审核不通过退款",false);
        //手续费退款，从临时总账退到用户上
        DdbIntegralAccount serviceTempAccount = ddbIntegralAccountMapper.selectByRealName(rule.getResaveServiceTempName());
        integerExchangeService.addInteg(ddbIntegralWithdrawRecord.getIntegService(),0-ddbIntegralWithdrawRecord.getIntegService(),ddbIntegralWithdrawRecord.getCustId(),
                ddbIntegralWithdrawRecord.getZid(),serviceTempAccount,"提现申请审核不通过手续费退款",false);
        if(update >0){
            //发送系统消息
            AppNoticeSubVo appNoticeSubVo = new AppNoticeSubVo();
            appNoticeSubVo.setCustId(ddbIntegralWithdrawRecord.getCustId());
            appNoticeSubVo.setViewCtime(DateUtils.getDateTime());
            appNoticeSubVo.setState(true);
            appNoticeSubVo.setTitle(shNotPassTitle);
            appNoticeSubVo.setSubTitle(shNotPassSubTitle);
            appNoticeSubVo.setBody(withdrawRecord.getRemark());
            appNoticeSubVo.setAppId(customer.getAppId());
            int noticeSub = appNoiceService.createNoticeSub(appNoticeSubVo);
            if(noticeSub > 0){
                resultMap.put("status","success");
            }else {
                resultMap.put("status","fail");
            }
        }else {
            resultMap.put("status","fail");
        }
        return resultMap;
    }

    /**
     * 修改申请记录
     * @param ddbIntegralWithdrawRecord
     * @param applyStatus
     * @param drawStatus
     * @param remark
     * @param admin
     * @param type 1审核，2提现
     * @return
     */
    private Integer updateWithDrawRecord(DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord, Integer applyStatus, Integer drawStatus, String remark, Admin admin,Integer type){
        DdbIntegralWithdrawRecord record = new DdbIntegralWithdrawRecord();
        record.setId(ddbIntegralWithdrawRecord.getId());
        if(applyStatus != 0){
            record.setApplyStatus(applyStatus);
        }
        if(drawStatus != 0){
            record.setDrawStatus(drawStatus);
            record.setOperator(admin.getUsername());
        }
        record.setAuditor(admin.getUsername());
        record.setRemark(remark);
        record.setUpdateTime(DateUtils.getDateTime());
        if(type == 1){
            record.setAuditTime(DateUtils.getDateTime());
        }
        if(type == 2){
            record.setDrawTime(DateUtils.getDateTime());
        }

        return ddbIntegralWithdrawRecordMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 提现通过
     * @param withdrawRecord
     * @param admin
     * @return
     */
    public Map<String,String> doSuccess(DdbIntegralWithdrawRecord withdrawRecord, Admin admin) {
        HashMap<String, String> resultMap = new HashMap<>();
        DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord = new DdbIntegralWithdrawRecord();
        ddbIntegralWithdrawRecord.setZid(withdrawRecord.getZid());
        ddbIntegralWithdrawRecord = ddbIntegralWithdrawRecordMapper.selectOne(ddbIntegralWithdrawRecord);
        DdbIntegralWithdrawRuleVo rule = ddbIntegralWithdrawRuleMapper.getRuleByZid(ddbIntegralWithdrawRecord.getWithdrawRuleId());
        Customer customer = customerMapper.selectByCustId(ddbIntegralWithdrawRecord.getCustId());
        if(withdrawRecord.getRemark() == null || "".equals(withdrawRecord.getRemark())){
            withdrawRecord.setRemark(txSuccessBody);
        }
        if(ddbIntegralWithdrawRecord.getDrawStatus() == 1){
            resultMap.put("status","fail");
            return resultMap;
        }
        Integer update = updateWithDrawRecord(ddbIntegralWithdrawRecord,0,1,withdrawRecord.getRemark(),admin,2);
        if(update > 0){
            //转账临时总账到回收总账
            ddbIntegralAccountService.accountToAccount(ddbIntegralWithdrawRecord.getAmount(),xhAccount,rule.getResaveIntegRealName(),ddbIntegralWithdrawRecord.getZid(),"现贝提现成功");
            ddbIntegralAccountService.accountToAccount(ddbIntegralWithdrawRecord.getIntegService(),rule.getResaveServiceRealName(),
            		rule.getResaveServiceTempName(),ddbIntegralWithdrawRecord.getZid(),"现贝提现成功,手续费转账到总账");
            
            //发送系统消息
            AppNoticeSubVo appNoticeSubVo = new AppNoticeSubVo();
            appNoticeSubVo.setCustId(ddbIntegralWithdrawRecord.getCustId());
            appNoticeSubVo.setViewCtime(DateUtils.getDateTime());
            appNoticeSubVo.setState(true);
            appNoticeSubVo.setTitle(txSuccessTitle);
            appNoticeSubVo.setSubTitle(txSuccessSubTitle);
            appNoticeSubVo.setBody(withdrawRecord.getRemark());
            appNoticeSubVo.setAppId(customer.getAppId());
            int noticeSub = appNoiceService.createNoticeSub(appNoticeSubVo);
        }else {
            resultMap.put("status","fail");
        }

        return resultMap;
    }

    /**
     * 提现不通过
     * @param withdrawRecord
     * @param admin
     * @return
     */
    public Map<String,String> doFail(DdbIntegralWithdrawRecord withdrawRecord, Admin admin) {
        HashMap<String, String> resultMap = new HashMap<>();
        DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord = new DdbIntegralWithdrawRecord();
        ddbIntegralWithdrawRecord.setZid(withdrawRecord.getZid());
        ddbIntegralWithdrawRecord = ddbIntegralWithdrawRecordMapper.selectOne(ddbIntegralWithdrawRecord);
        Customer customer = customerMapper.selectByCustId(ddbIntegralWithdrawRecord.getCustId());
        //退款，临时总账到个人账户
        DdbIntegralWithdrawRuleVo rule = ddbIntegralWithdrawRuleMapper.getRuleByZid(ddbIntegralWithdrawRecord.getWithdrawRuleId());
        DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(rule.getResaveIntegRealName());
        if(withdrawRecord.getRemark() == null || "".equals(withdrawRecord.getRemark())){
            withdrawRecord.setRemark(txFailBody);
        }
        if(ddbIntegralWithdrawRecord.getDrawStatus() == 2){
            resultMap.put("status","fail");
            return resultMap;
        }
        Integer update = updateWithDrawRecord(ddbIntegralWithdrawRecord,0,2,withdrawRecord.getRemark(),admin,2);
        if(update > 0){
            integerExchangeService.addInteg(ddbIntegralWithdrawRecord.getAmount(),0-ddbIntegralWithdrawRecord.getAmount(),ddbIntegralWithdrawRecord.getCustId(),
                    ddbIntegralWithdrawRecord.getZid(),ddbIntegralAccount,"提现申请提现不通过退款",false);
            //提现失败，手续费从临时总账退到用户钱包
            DdbIntegralAccount serviceTempAccount = ddbIntegralAccountMapper.selectByRealName(rule.getResaveServiceTempName());
            integerExchangeService.addInteg(ddbIntegralWithdrawRecord.getIntegService(),0-ddbIntegralWithdrawRecord.getIntegService(),
            		ddbIntegralWithdrawRecord.getCustId(), ddbIntegralWithdrawRecord.getZid(),serviceTempAccount,"提现申请提现不通过退款",false);
            //发送系统消息
            AppNoticeSubVo appNoticeSubVo = new AppNoticeSubVo();
            appNoticeSubVo.setCustId(ddbIntegralWithdrawRecord.getCustId());
            appNoticeSubVo.setViewCtime(DateUtils.getDateTime());
            appNoticeSubVo.setState(true);
            appNoticeSubVo.setTitle(txFailTitle);
            appNoticeSubVo.setSubTitle(txFailSubTitle);
            appNoticeSubVo.setBody(withdrawRecord.getRemark());
            appNoticeSubVo.setAppId(customer.getAppId());
            appNoiceService.createNoticeSub(appNoticeSubVo);
        }else {
            resultMap.put("status","fail");
        }

        return resultMap;
    }
}
