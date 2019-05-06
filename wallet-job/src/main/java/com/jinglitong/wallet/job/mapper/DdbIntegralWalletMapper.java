package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DdbIntegralWalletMapper extends MyMapper<DdbIntegralWallet> {

    DdbIntegralWallet selectByRealNameAdnCustId(String realName, String custId);

    int addInteger(int amount, String realName, String custId);
    
    int insertBf();
    
}