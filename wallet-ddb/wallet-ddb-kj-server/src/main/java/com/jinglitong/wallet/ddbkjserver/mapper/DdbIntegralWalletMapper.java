package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import feign.Param;
import org.springframework.stereotype.Component;

@Component
public interface DdbIntegralWalletMapper extends MyMapper<DdbIntegralWallet> {

    DdbIntegralWallet selectByRealNameAdnCustId(String realName, String custId);

    int addInteger(int amount, String realName, String custId);
    
    int insertBf();
    
}