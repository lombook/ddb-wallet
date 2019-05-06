package com.jinglitong.springshop.servcie;

import com.alibaba.fastjson.JSON;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.enumeration.AssuredAccountEnum;
import com.jinglitong.springshop.enumeration.CurrencyCodeEnum;
import com.jinglitong.springshop.enumeration.IntegralCustRecordTypeEnum;
import com.jinglitong.springshop.enumeration.RealNameEnum;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName OrderCancelService
 * @Description 订单取消，手动取消和自动取消
 * @Author zili.zong
 * @Date 2019/3/25 16:43
 * @Version 1.0
 **/
@Slf4j
@Service
public class IntegralPayCancelService {


    @Resource
    private DdbCustIntegralRecordMapper integralCustRecordMapper;

    @Resource
    private DdbIntegralWalletSeqMapper integralWalletSeqMapper;

    @Resource
    private DdbIntegralWalletMapper integralWalletMapper;

    @Resource
    private OrderRealPayMapper orderRealPayMapper;

    @Resource
    private DdbIntegralAccountMapper integralAccountMapper;

    /**
     * desc 积分支付退回
     * @param mainOrderId
     * @param custId
     * @return
     */
    @Transactional
    public boolean integralPayCancel(String mainOrderId,String custId){
        boolean result = false;
        OrderRealPay queryPayParam = new OrderRealPay();
        queryPayParam.setOrderId(mainOrderId);
        List<OrderRealPay> list = orderRealPayMapper.select(queryPayParam);
        Example example = new Example(DdbIntegralAccount.class);
        List<String> queryList = new ArrayList();
        queryList.add(RealNameEnum.TYPE_WB_INCOME.getType());
        queryList.add(RealNameEnum.TYPE_XB_INCOME.getType());
        queryList.add(RealNameEnum.TYPE_BF_INCOME.getType());
        example.createCriteria().andIn("realName",queryList);
        List<DdbIntegralAccount> accounts = integralAccountMapper.selectByExample(example);
        if(list!=null&&list.size()>0) {
            for(OrderRealPay pay : list){
                //物贝和现贝抵扣
                if((pay.getCurrencyCode().equals(CurrencyCodeEnum.CODE_XB.getCode())||
                        pay.getCurrencyCode().equals(CurrencyCodeEnum.CODE_WB.getCode()))&&
                        pay.getAmount().compareTo(BigDecimal.ZERO)==1){
                    deductionCanel(accounts,pay,custId,pay.getCurrencyCode());
                    result = true;
                }
                //宝分积分支付
                if(pay.getCurrencyCode().equals(CurrencyCodeEnum.CODE_BF.getCode())&&pay.getAmount().compareTo(BigDecimal.ZERO)==1){
                    bfPayCanel(accounts,pay,custId);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * desc 物贝和现贝抵扣 退回
     * @param pay
     * @param custId
     * @return
     */
    private void deductionCanel(List<DdbIntegralAccount> accounts,OrderRealPay pay,String custId,String currencyCode){
        for(DdbIntegralAccount account : accounts){
            if(account.getRealName().equals(RealNameEnum.TYPE_WB_INCOME.getType())||account.getRealName().equals(RealNameEnum.TYPE_XB_INCOME.getType())){
            	if(account.getIntegName().equals(pay.getCurrencyCode())){
	                //抵扣
	                int xjdNum = integralWalletUpdate(custId,pay.getCurrencyCode(),pay.getAmount().multiply(new BigDecimal(-100)).longValue());
	                if(xjdNum!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3105.getCode(),ErrorEnum.ERROR_3105.getMsg()));
	                }
	                String remark = currencyCode.equals(CurrencyCodeEnum.CODE_WB.getCode()) ? "物贝抵扣法币支付转出退回":"现贝抵扣法币支付转出退回" ;
	                int custRecordNum = integralCustRecordLog(account,custId,pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_1.getType(),pay.getOrderId(),remark);
	                if(custRecordNum!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3112.getCode(),ErrorEnum.ERROR_3112.getMsg()));
	                }
	                int walletSeq =integralWalletSeqLog(account,custId,pay.getAmount(), IntegralCustRecordTypeEnum.TYPE_1.getType(),pay.getOrderId());
	                if(walletSeq!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3113.getCode(),ErrorEnum.ERROR_3113.getMsg()));
	                }
	                String remark2 = currencyCode.equals(CurrencyCodeEnum.CODE_WB.getCode()) ? "物贝账号物贝抵扣法币转入退出":"现贝账号现贝抵扣法币转入退出" ;
	                //抵扣入账指定账户
	                int xjdIncomeNum = integralWalletUpdate(AssuredAccountEnum.XJD.getAccount(),pay.getCurrencyCode(),pay.getAmount().multiply(new BigDecimal(100)).longValue());
	                if(xjdIncomeNum!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3114.getCode(),ErrorEnum.ERROR_3114.getMsg()));
	                }
	                String fixedWalletId = currencyCode.equals(CurrencyCodeEnum.CODE_WB.getCode()) ? AssuredAccountEnum.WB.getAccount() : AssuredAccountEnum.XB.getAccount();
	                int custRecordIncomeNum = integralCustRecordLog(account,fixedWalletId,pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_2.getType(),pay.getOrderId(),remark2);
	                if(custRecordIncomeNum!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3115.getCode(),ErrorEnum.ERROR_3115.getMsg()));
	                }
	                int walletIncomeSeq =integralWalletSeqLog(account,fixedWalletId,pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_2.getType(),pay.getOrderId());
	                if(walletIncomeSeq!=1){
	                    throw new RuntimeException(getString(ErrorEnum.ERROR_3116.getCode(),ErrorEnum.ERROR_3116.getMsg()));
	                }
	                break;
            	}
            }
        }
    }
    /**
     * desc 宝分积分支付 退回
     * @param pay
     * @param custId
     * @return
     */
    public void bfPayCanel(List<DdbIntegralAccount> accounts,OrderRealPay pay,String custId){
        for(DdbIntegralAccount account : accounts) {
            if (account.getRealName().equals(RealNameEnum.TYPE_BF_INCOME.getType())) {
                //宝分扣除
                int bfNum = integralWalletUpdate(custId,pay.getCurrencyCode(),pay.getAmount().multiply(new BigDecimal(-100)).longValue());
                if(bfNum!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3124.getCode(),account.getRealCname()+ErrorEnum.ERROR_3124.getMsg()));
                }
                int custRecordNum = integralCustRecordLog(account,custId,pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_1.getType(),pay.getOrderId(),"宝分积分支付转出退回");
                if(custRecordNum!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3125.getCode(),account.getRealCname()+ErrorEnum.ERROR_3125.getMsg()));
                }
                int walletSeq =integralWalletSeqLog(account,custId,pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_1.getType(),pay.getOrderId());
                if(walletSeq!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3126.getCode(),account.getRealCname()+ErrorEnum.ERROR_3126.getMsg()));
                }
                //宝分扣除入账指定账户
                int bfIncomeNum = integralWalletUpdate(AssuredAccountEnum.BF.getAccount(),pay.getCurrencyCode(),pay.getAmount().multiply(new BigDecimal(100)).longValue());
                if(bfIncomeNum!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3127.getCode(),account.getRealCname()+ErrorEnum.ERROR_3127.getMsg()));
                }
                int custRecordIncomeNum = integralCustRecordLog(account, AssuredAccountEnum.BF.getAccount(),pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_2.getType(),pay.getOrderId(),"宝分积分支付转入退出");
                if(custRecordIncomeNum!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3128.getCode(),account.getRealCname()+ErrorEnum.ERROR_3128.getMsg()));
                }
                int walletIncomeSeq =integralWalletSeqLog(account,AssuredAccountEnum.BF.getAccount(),pay.getAmount(),IntegralCustRecordTypeEnum.TYPE_2.getType(),pay.getOrderId());
                if(walletIncomeSeq!=1){
                    throw new RuntimeException(getString(ErrorEnum.ERROR_3129.getCode(),account.getRealCname()+ErrorEnum.ERROR_3129.getMsg()));
                }
                break;
            }
        }
    }

    private int integralCustRecordLog(DdbIntegralAccount integralAccount,String custId,BigDecimal amount,int type,String flowId,String remark){
    	DdbCustIntegralRecord custRecord = new DdbCustIntegralRecord();
        Date now = new Date(System.currentTimeMillis());
        custRecord.setCreateTime(now);
        custRecord.setUpdateTime(now);
        custRecord.setZid(UUID.randomUUID().toString().replace("-",""));
        custRecord.setAmount(amount.multiply(new BigDecimal(100)).intValue());
        custRecord.setCustId(custId);
        custRecord.setFlowId(flowId);
        custRecord.setIntegCname(integralAccount.getIntegCname());
        custRecord.setIntegName(integralAccount.getIntegName());
        custRecord.setRealCname(integralAccount.getRealCname());
        custRecord.setRealName(integralAccount.getRealName());
        custRecord.setInteZid(integralAccount.getZid());
        custRecord.setRemark(remark);
        custRecord.setType(type);
        return integralCustRecordMapper.insertSelective(custRecord);
    }

    private int integralWalletSeqLog(DdbIntegralAccount integralAccount,String custId,BigDecimal amount,int type,String flowId){
        DdbIntegralWallet integralWallet = integralWalletMapper.selectByRealNameAdnCustId(integralAccount.getIntegName(),custId);
        DdbIntegralWalletSeq integralWalletSeq = new DdbIntegralWalletSeq();
        integralWalletSeq.setZid(UuidUtil.getUUID());
        integralWalletSeq.setCustId(custId);
        integralWalletSeq.setRealCname(integralAccount.getRealCname());
        integralWalletSeq.setRealName(integralAccount.getRealName());
        integralWalletSeq.setInteCname(integralAccount.getIntegCname());
        integralWalletSeq.setInteName(integralAccount.getIntegName());
        integralWalletSeq.setAfterAmount(integralWallet.getAmount());
        integralWalletSeq.setAmount(amount.multiply(new BigDecimal(100)).longValue());
        if(type==1){
            integralWalletSeq.setBeforeAmount(integralWallet.getAmount()- amount.multiply(new BigDecimal(100)).longValue());
        }else {
            integralWalletSeq.setBeforeAmount(integralWallet.getAmount()+ amount.multiply(new BigDecimal(100)).longValue());
        }
        Date now = new Date(System.currentTimeMillis());
        integralWalletSeq.setCreateTime(now);
        integralWalletSeq.setUpdateTime(now);
        integralWalletSeq.setType(type);
        integralWalletSeq.setFlowId(flowId);
        return integralWalletSeqMapper.insert(integralWalletSeq);
    }

    private int  integralWalletUpdate(String custId,String inteName,long amount){
        DdbIntegralWallet wallet = new DdbIntegralWallet();
        wallet.setUpdateTime(new Date(System.currentTimeMillis()));
        wallet.setAmount(amount);
        wallet.setInteName(inteName);
        wallet.setCustId(custId);
        return integralWalletMapper.updateByPay(wallet);
    }


    private String getString(int code,String msg){
        Map map = new HashMap<>();
        map.put("code", code);
        map.put("msg",msg);
        return JSON.toJSONString(map);
    }


}
