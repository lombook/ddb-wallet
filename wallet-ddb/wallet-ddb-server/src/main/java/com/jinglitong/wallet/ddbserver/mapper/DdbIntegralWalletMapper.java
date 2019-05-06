package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import feign.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbIntegralWalletMapper extends MyMapper<DdbIntegralWallet> {

    DdbIntegralWallet selectByRealNameAdnCustId(String realName, String custId);

    int addInteger(int amount, String realName, String custId);
    
    int insertBf();

    List<DdbIntegralWallet> selectByCustId(String custId);
}