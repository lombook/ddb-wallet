package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinglitong.springshop.entity.DdbIntegralWallet;
import com.jinglitong.springshop.mapper.DdbIntegralWalletMapper;
import com.jinglitong.springshop.vo.response.IntegralWalletVo;
import com.jinglitong.springshop.vo.response.UserAssetVo;

@Service
public class IntegralWalletService {

	@Autowired
	private DdbIntegralWalletMapper integralWalletMapper;
	
	public IntegralWalletVo getWallet(String zid){
		DdbIntegralWallet record = new DdbIntegralWallet();
		record.setCustId(zid);
		record.setInteName("xjd_all");
		DdbIntegralWallet wallet = integralWalletMapper.selectOne(record);
		IntegralWalletVo vo = new IntegralWalletVo();
		if(wallet != null) {
			String amount = new BigDecimal(wallet.getAmount()).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString();
			vo.setAmount(amount);
			vo.setInteName(wallet.getInteName());
		}
		return vo;
	}
	
	public UserAssetVo getUserAssetAmount(String custId) {
		// TODO Auto-generated method stub
		DdbIntegralWallet ddbIntegralWallet = new DdbIntegralWallet();
		ddbIntegralWallet.setCustId(custId);
		List<DdbIntegralWallet> assetsList = integralWalletMapper.select(ddbIntegralWallet);
		UserAssetVo vo = new UserAssetVo();
		for (DdbIntegralWallet assets : assetsList) {

			String inteRealName = assets.getInteName() == null ? "" : assets.getInteName();
			// Integer amount = assets.getAmount()==null?0:(assets.getAmount()/100);//
			// 以分为单位,需要转换为元
			Long amount = assets.getAmount() == null ? 0 : assets.getAmount();
			BigDecimal amountVo = new BigDecimal(amount).divide(new BigDecimal("100"));

			// 根据类型 判断 去赋值
			if ("djb_all".equals(inteRealName)) {// 宝分 djb_all
				vo.setDjbType(inteRealName);
				vo.setDjbAmount(amountVo);

			} else if ("xjd_all".equals(inteRealName)) {// 现金贷shubei_all
				vo.setXjdNum(amountVo);
				vo.setXjdType(inteRealName);
			
			} else if ("swtc_all".equals(inteRealName)) {// swtcmiaobei_all
				vo.setSwtcType(inteRealName);
				vo.setSwtcAmount(amountVo);
			} 
		}
		
		return vo;

	}
}
