package com.jinglitong.wallet.ddbserver.service;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbDigAssetTodayDemand;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbDigAssetTodayDemandMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class DdbIntegralWalletService {

	@Autowired
	private RequireHistoryService requireHistoryService;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private DdbIntegralAccountService ddbIntegralAccountService;

	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;

	@Autowired
	private DdbDigAssetTodayDemandMapper ddbDigAssetTodayDemandMapper;

	@Value("${bf_caizai}")
	private String bfChangjing;

/**
 * 接收宝分
 * @param bf
 */
	public void receiveBf(Map<String, Object> bf,String date) {
		DdbDigAssetTodayDemand ddbDigAssetTodayDemand = new DdbDigAssetTodayDemand();
		ddbDigAssetTodayDemand.setAmount(bf.get("meiNum").toString());
		ddbDigAssetTodayDemand.setCreateTime(date);
		ddbDigAssetTodayDemand.setCustId(bf.get("userId").toString());
		ddbDigAssetTodayDemand.setFlowId(bf.get("flowId").toString());
		ddbDigAssetTodayDemandMapper.insert(ddbDigAssetTodayDemand);

        Customer customer = customerMapper.selectByCustId(bf.get("userId").toString());
        if(customer == null){
            throw new RuntimeException("用户未找到"+bf.get("userId").toString());
        }
		//排重存储
		requireHistoryService.ddbRequireHistoryInsert(bf.get("flowId").toString(),date);
        DdbCustIntegralWallet ddbCustIntegralWallet = new DdbCustIntegralWallet();
        String bfn = bf.get("meiNum")+"";
		BigDecimal bfAmount = new BigDecimal(bfn);
		BigDecimal num = bfAmount.multiply(new BigDecimal("10000"));
		//Float num1 = Float.parseFloat(bfn)*10000;
		//int intValue = num1.intValue();
		ddbCustIntegralWallet.setBaofen(num.intValue());
        ddbCustIntegralWallet.setCustId(customer.getCustId());
		DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(bfChangjing);
		if(ddbIntegralAccount == null){
			throw new RuntimeException("缺少总账宝分采摘"+bfChangjing);
		}
		ddbIntegralAccountService.addInteg(num.intValue(),bf.get("userId").toString(),bf.get("flowId").toString(),ddbIntegralAccount,"宝分采摘");
	}

}
