package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

public interface DdbAssetAccountMapper extends MyMapper<DdbAssetAccount> {
    DdbAssetAccount selectByZid(String s);
}