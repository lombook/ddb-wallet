package com.jinglitong.wallet.reportserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.view.DdbAssetDisplayVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWalletVo;
import com.jinglitong.wallet.reportserver.util.MyMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface DdbIntegralWalletMapper extends MyMapper<DdbIntegralWallet> {

    DdbIntegralWallet selectByRealNameAdnCustId(String realName, String custId);

    int addInteger(int amount, String realName, String custId);
    
    int insertBf();
    
    List<DdbAssetDisplayVo> getAsset(@Param("appId")String appId,@Param("account")String account);
    
    List<DdbCustIntegralRecord> getRecord(DdbIntegralWalletVo wallet);
    
}