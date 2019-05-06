package com.jinglitong.wallet.checkserver.service;

import com.jinglitong.wallet.checkserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class DdbIntegralAccountService {

    @Autowired
    private DdbIntegralAccountMapper ddbIntegralAccountMapper;
    @Autowired
	private AliCloudMailService aliCloudMailService;
    
    @Value("${check.mailto}")
	private String toAddress;
	
	@Value("${check.subject}")
	private String subject;


    public boolean getCheck(String beginTime, String endTime) {  	
    	boolean res = true;
        StringBuffer error = new StringBuffer("");
        //总体校验三张表值是否相等
        log.info("稽核校验开始........");

        //按照inteName分类 校验相应总账是否一样 每种inteName 给总账转账的record = 总账 + 所有用户钱包对应inteName之和，说明总账平的
        List<Map<String,String>> ddbInteNames = ddbIntegralAccountMapper.selectDdbIntegralNames();
        for (Map<String,String>ddbInteName:ddbInteNames) {        	
        	List<Long> amountlist = ddbIntegralAccountMapper.selectDdbIntegralByIntegName(ddbInteName.get("integ_name"));        
        	Long accountAmount = 0L;
        	Long recordAmount = 0L;
        	Long walletAmount = 0L;
        	if(amountlist.get(0) != null){
        		recordAmount = amountlist.get(0);
        	}
        	if(amountlist.get(1) != null){
        		accountAmount = amountlist.get(1);
        	}
        	if(amountlist.get(2) != null){
        		walletAmount = amountlist.get(2);
        	}
        	Long sub = accountAmount + walletAmount;
        	Long res1 = sub - recordAmount;

        	if( res1 != 0 ){
        		if(error.toString().length() < 1){
        			error.append("稽核校验不通过。<br>开始时间：" + beginTime + "结束时间：" + endTime);	
        		}
        		error.append("<br>" + "总账向分账转" +ddbInteName.get("integ_cname")+":" +  recordAmount);
        		error.append("<br>" + "目前" + ddbInteName.get("integ_cname") + "分账有:" + accountAmount);
        		error.append("<br>" + "个人钱包有:" + walletAmount);
        		error.append("<br>" + "差值:" + Math.abs(res1));
        		
        	}
   
        }

        //校验个人账户
        //在ddb_integral_wallet_seq中查找所有的用户cust_id
        List<Map<String, Object>> custList = ddbIntegralAccountMapper.selectCustId(beginTime, endTime);
        //在总帐表中找到所有的integ_name
        List<DdbIntegralAccount> ddbIntegraAccountIntegNames = ddbIntegralAccountMapper.selectIntegeName(beginTime, endTime);
        for (Map<String, Object> cust : custList) {
            for (DdbIntegralAccount ddbAccount : ddbIntegraAccountIntegNames) {
            	//对每个用户的每个integ_name来说查找seq 
            	// 1、找到时间段内最后交易后的值 after
            	// 2、和时间段内开始交易的初始值 before（用时间段内之前的一条记录交易后值作为时间段内开市交易的初始值，如果有的话，否则就按照0）
            	// 3、找到时间段内增加值的和add（不带符号）、减少的值的和 sub（不带符号）
            	// 4、检查 before + add - sub = after 说明是平的
                Map<String, Object> mapAccountBitMax = ddbIntegralAccountMapper.selectWalletRealNameBit(beginTime, endTime, ddbAccount.getIntegName(), "DESC", cust.get("cust_id").toString());
                int beforeAmount = 0 ;
                Map<String, Object> mapAccountBitMin = ddbIntegralAccountMapper.selectWalletRealNameBitbefore(beginTime, endTime, ddbAccount.getIntegName(), "DESC", cust.get("cust_id").toString());
               if(mapAccountBitMin == null){
                   /*mapAccountBitMin = ddbIntegralAccountMapper.selectWalletRealNameBit(beginTime, endTime, ddbAccount.getIntegName(), "ASC", cust.get("cust_id").toString());
                   if(mapAccountBitMin != null){
                       beforeAmount = new BigDecimal(mapAccountBitMin.get("minam").toString()).intValue();
                   }*/
               }else {
                   if(mapAccountBitMin != null){
                       beforeAmount = new BigDecimal(mapAccountBitMin.get("mixam").toString()).intValue();
                   }
               }
                Map<String, Object> mapWalletRealAmountSub = ddbIntegralAccountMapper.selectRealWalletType(beginTime, endTime, 2, ddbAccount.getIntegName(), cust.get("cust_id").toString());
                Map<String, Object> mapWalletRealAmountadd = ddbIntegralAccountMapper.selectRealWalletType(beginTime, endTime, 1, ddbAccount.getIntegName(), cust.get("cust_id").toString());

                Map<String, Object> mapRecordRealAmountSub = ddbIntegralAccountMapper.selectRealRecordType(beginTime, endTime, 2, ddbAccount.getIntegName(), cust.get("cust_id").toString());
                Map<String, Object> mapRecordRealAmountadd = ddbIntegralAccountMapper.selectRealRecordType(beginTime, endTime, 1, ddbAccount.getIntegName(), cust.get("cust_id").toString());

                int subRecord = 0;
                int addRecord = 0;
                int subsuman = 0;
                int addsuman = 0;

                int afterAmount = 0;

                if(mapAccountBitMax != null){
                    afterAmount = new BigDecimal(mapAccountBitMax.get("mixam").toString()).intValue();
                }

                if (mapRecordRealAmountSub != null) {
                    subRecord = new BigDecimal(mapRecordRealAmountSub.get("sumam").toString()).intValue();
                }
                if (mapRecordRealAmountadd != null) {
                    addRecord = new BigDecimal(mapRecordRealAmountadd.get("sumam").toString()).intValue();
                }


                if (mapWalletRealAmountSub != null) {
                    subsuman = new BigDecimal(mapWalletRealAmountSub.get("sumam").toString()).intValue();
                }
                if (mapWalletRealAmountadd != null) {
                    addsuman = new BigDecimal(mapWalletRealAmountadd.get("sumam").toString()).intValue();
                }
                if ((mapWalletRealAmountSub != null || mapWalletRealAmountadd != null)
                        && (mapRecordRealAmountSub != null || mapRecordRealAmountadd != null) ) {
                    int consumAmount = addsuman -subsuman;
                    int consumRecored = addRecord -subRecord;
                    
                    int sumRecord = beforeAmount + consumRecored;
                    int sumAmout = beforeAmount + consumAmount;
                    
                    
                    if (sumAmout != afterAmount || sumRecord != afterAmount) {
                       Map<String,String> customer = ddbIntegralAccountMapper.selectCustomerByCustId(cust.get("cust_id").toString());
                       if(error.toString().length() < 1){
                    	   error.append("稽核校验不通过。<br>开始时间：" + beginTime + " 结束时间：" + endTime);	
               			}	
                     /*  error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化前值：" + beforeAmount)  ;
                        error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化后值：" + afterAmount)  ;
                        error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化值：" + -consumAmount)  ;
                        error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "流水变化值：" + -consumRecore)  ;
                        error.append("<br>" + "钱包变化前值 + 钱包变化值 = 钱包变化后值 ，  钱包差值 =钱包化前值 + 钱包变化值 -  钱包变化后值  ") ;
                        error.append("<br>" + "钱包变化前值 + 流水变化值 = 钱包变化后值  ，   流水差值 =钱包化前值 + 流水变化值 -  钱包变化后值")  ;
                        error.append("<br> 钱包差值：" + Math.abs(beforeAmount - sum1) +"<br> 流水差值：" + Math.abs(sumRecord - beforeAmount) +  "  已锁定。用户custId："+cust.get("cust_id").toString().toString()+"。")  ;*/

                      /* error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化前值：" + beforeAmount)  ;
                       error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化后值：" + afterAmount)  ;
                       error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "钱包变化值：" + -consumAmount)  ;
                       error.append("<br>" + "用户账户:" + customer.get("account").toString() + ddbAccount.getIntegCname() + "流水变化值：" + -consumRecore)  ;*/
                       
                       String symbolAmount = "=";
                       if(sumAmout > afterAmount){
                    	   symbolAmount = ">";
                       }else if(sumAmout < afterAmount){
                    	   symbolAmount = "<";
                       }
                       String symbolRecored = "=";
                       if(sumRecord > afterAmount){
                    	   symbolRecored = ">";
                       }else if(sumRecord < afterAmount){
                    	   symbolRecored = "<";
                       }
                       
                       error.append("<br><br>" + "用户账户:" + customer.get("account").toString() + " 帐不平");
                       error.append("<br>" + "钱包变化前值("+ beforeAmount +") + 钱包变化值("+ consumAmount + ") " + symbolAmount + " 钱包变化后值("+ afterAmount +")，  钱包差值 = "+ Math.abs(afterAmount - sumAmout) );
                       error.append("<br>" + "钱包变化前值 ("+ beforeAmount +") + 流水变化值 ("+ consumRecored + ") " + symbolRecored + " 钱包变化后值("+ afterAmount +")，   流水差值 ="+ Math.abs(sumRecord - afterAmount) );
                       error.append("<br> 已锁定。用户custId："+cust.get("cust_id").toString().toString()+"。")  ;
                        Map<String, Object> map = new HashMap<>();
                        map.put("remark", "账目不平锁定");
                        map.put("state", 0);
                        map.put("cust_id", cust.get("cust_id").toString());
                        ddbIntegralAccountMapper.updCustomerStatus(map);

                        log.info("个人账户出错"+cust);
                        res = false;
                    }
                }
            }
        }
      if(error.length() > 0){
    	  aliCloudMailService.sendMail(toAddress, subject, error.toString());
    	  log.info("稽核校验不通过，帐不平........"+error);
      }else{
    	  log.info("稽核校验通过，平帐........");
      }
      log.info("稽核校验结束........");
      return res;
    }
}