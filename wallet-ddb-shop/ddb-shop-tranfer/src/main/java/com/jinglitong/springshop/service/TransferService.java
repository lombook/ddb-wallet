package com.jinglitong.springshop.service;

import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.ProfileInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName TransferService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/3/18 17:39
 * @Version 1.0
 **/
@Service
@Slf4j
public class TransferService {
    @Autowired
    private DdbIntegralAccountMapper integralAccountMapper;

    @Autowired
    private DdbIntegralWalletMapper integralWalletMapper;

    @Autowired
    private DdbCustIntegralRecordMapper integralCustRecordMapper;

    @Autowired
    private DdbIntegralAccountSeqMapper integralAccountSeqMapper;

    @Autowired
    private DdbIntegralWalletSeqMapper integralWalletSeqMapper;


    @Value("${payAcc.email.address}")
    private String payAccEmailAddress;

    @Value("${payAcc.amount}")
    private Long payAccAmount;

    @Resource
    private MailService mailService;

    /**
     * 转账总账到个人积分或个人积分到总站
     * @param amount  转账金额 amount 为正数
     * @param custId   用户id
     * @param accountZid 总账zid
     * @param flowId    订单号
     * @param type  转账类型  1 总账到个人钱包，2 个人钱包到总账
     * @param remark 转账备注
     */
    @Transactional
    public Boolean tranferAccount(Long amount,String custId,String accountZid,String flowId, Integer type,String remark){
        Boolean flag = false;
        Date date = new Date();
        DdbIntegralAccount integralAccount = integralAccountMapper.selectByZid(accountZid);
        DdbIntegralWallet integralWallet = null;
        integralWallet = integralWalletMapper.selectByRealNameAdnCustId(integralAccount.getIntegName(),custId);
        if(accountZid == null){
            throw new RuntimeException("总账账户不存在accountZid = "+accountZid);
        }else {
            ProfileInfoVO info = new ProfileInfoVO("dm.ap-southeast-1.aliyuncs.com","ap-southeast-1","Dm","dm.ap-southeast-1.aliyuncs.com");
            //类型为1 验证总账金额是否小于指定金额
            if(type.equals(1)){
                //判断总账金额是否小于指定金额
                if(integralAccount.getAmount() < payAccAmount ) {
                    mailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + accountZid ,info);
                }
                //判断总账金额是否小于转账金额
                if(integralAccount.getAmount() < amount ) {
                    mailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + accountZid ,info);
                    throw new RuntimeException("总账账户余额不足zid = "+accountZid);
                }
            }else if (type.equals(2)){
                //类型为2
                //验证个人钱包金额是否小于转账金额
                if(integralWallet.getAmount() < amount ) {
                    mailService.sendMail(payAccEmailAddress, "总账余额不足，请注意","账户扣除不足 zid=" + integralWallet.getZid() ,info);
                    throw new RuntimeException("个人账户余额不足zid:"+accountZid);
                }
            }
        }
        //根据总账zid查询总账
        if(integralAccount == null){
            throw new RuntimeException("总账账户不存在"+accountZid);
        }
        if(type.equals(1)){
            //总账减少
            int aupdate = integralAccountMapper.addInteger(amount * -1, integralAccount.getZid());
            if(aupdate ==0){
                throw new RuntimeException("总账账户余额不足zid = "+integralAccount.getZid());
            }
            //个人钱包增加
            //积分增加
            if(integralWallet == null){
                integralWallet = new DdbIntegralWallet();
                integralWallet.setZid(UuidUtil.getUUID());
                integralWallet.setCustId(custId);
                integralWallet.setInteCname(integralAccount.getIntegCname());
                integralWallet.setInteName(integralAccount.getIntegName());
                integralWallet.setAmount(0L);
                integralWallet.setCreateTime(date);
                integralWallet.setUpdateTime(date);
                int insert = integralWalletMapper.insert(integralWallet);
                if(insert <= 0){
                    throw new RuntimeException("钱包创建失败!");
                }
            }
            int wupdate  = integralWalletMapper.addInteger(amount,integralAccount.getIntegName(),custId);
            if(wupdate ==0){
                throw new RuntimeException("总账账户余额不足zid:"+integralAccount.getZid());
            }
        }else if (type.equals(2)){
            //总账增加
            int aupdate = integralAccountMapper.addInteger(amount, integralAccount.getZid());
            if(aupdate ==0){
                throw new RuntimeException("总账账户余额不足zid = "+integralAccount.getZid());
            }
            //个人钱包减少
            integralWallet = integralWalletMapper.selectByRealNameAdnCustId(integralAccount.getIntegName(),custId);
            if(integralWallet == null){
                integralWallet = new DdbIntegralWallet();
                integralWallet.setZid(UuidUtil.getUUID());
                integralWallet.setCustId(custId);
                integralWallet.setInteCname(integralAccount.getIntegCname());
                integralWallet.setInteName(integralAccount.getIntegName());
                integralWallet.setAmount(0L);
                integralWallet.setCreateTime(date);
                integralWallet.setUpdateTime(date);
                integralWalletMapper.insert(integralWallet);
            }
            int wupdate  = integralWalletMapper.addInteger(0-amount,integralAccount.getIntegName(),custId);
            if(wupdate ==0){
                throw new RuntimeException("总账账户余额不足zid = "+integralAccount.getZid());
            }
        }
        //记录日志
        //入record表
        DdbCustIntegralRecord integralCustRecord = new DdbCustIntegralRecord();
        integralCustRecord.setZid(UuidUtil.getUUID());
        integralCustRecord.setCustId(custId);
        integralCustRecord.setAmount(Integer.parseInt(String.valueOf(amount)));
        integralCustRecord.setInteZid(accountZid);
        integralCustRecord.setRealName(integralAccount.getRealName());
        integralCustRecord.setRealCname(integralAccount.getRealCname());
        integralCustRecord.setIntegName(integralAccount.getIntegName());
        integralCustRecord.setIntegCname(integralAccount.getIntegCname());
        integralCustRecord.setHash("");
        integralCustRecord.setFlowId(flowId);
        integralCustRecord.setRemark(remark);
        integralCustRecord.setType(type);
        integralCustRecord.setCreateTime(date);
        integralCustRecord.setUpdateTime(date);
        integralCustRecordMapper.insert(integralCustRecord);
        //入walletseq表
        DdbIntegralWalletSeq integralWalletSeq = new DdbIntegralWalletSeq();
        integralWalletSeq.setZid(UuidUtil.getUUID());
        integralWalletSeq.setCustId(custId);
        integralWalletSeq.setRealCname(integralAccount.getRealCname());
        integralWalletSeq.setRealName(integralAccount.getRealName());
        integralWalletSeq.setInteCname(integralAccount.getIntegCname());
        integralWalletSeq.setInteName(integralAccount.getIntegName());
        integralWalletSeq.setBeforeAmount(integralWallet.getAmount());
        integralWalletSeq.setAmount(amount);
        if(type.equals(1)){
            integralWalletSeq.setAfterAmount(integralWallet.getAmount() + amount);
        }else {
            integralWalletSeq.setAfterAmount(integralWallet.getAmount() - amount);

        }
        integralWalletSeq.setType(type);
        integralWalletSeq.setCreateTime(date);
        integralWalletSeq.setUpdateTime(date);
        integralWalletSeq.setFlowId(flowId);
        integralWalletSeqMapper.insert(integralWalletSeq);
        //入accountseq表
        DdbIntegralAccountSeq integralAccountSeq = new DdbIntegralAccountSeq();
        integralAccountSeq.setZid(UuidUtil.getUUID());
        integralAccountSeq.setFlowId(flowId);
        integralAccountSeq.setInteId(accountZid);
        integralAccountSeq.setRealCname(integralAccount.getRealCname());
        integralAccountSeq.setRealName(integralAccount.getRealName());
        integralAccountSeq.setInteCname(integralAccount.getIntegCname());
        integralAccountSeq.setInteName(integralAccount.getIntegName());
        integralAccountSeq.setBeforeAmount(integralAccount.getAmount());
        integralAccountSeq.setAmount(amount);
        if(type.equals(1)){
            integralAccountSeq.setAfterAmount(integralAccount.getAmount() - amount);
            integralAccountSeq.setType(2);
        }else {
            integralAccountSeq.setAfterAmount(integralAccount.getAmount() + amount);
            integralAccountSeq.setType(1);
        }
        integralAccountSeq.setCreateTime(date);
        integralAccountSeq.setUpdateTime(date);
        integralAccountSeqMapper.insert(integralAccountSeq);
        flag = true;
        return flag;
    }
}
