package com.jinglitong.wallet.ddbkjserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

public interface DdbAssetAccountMapper extends MyMapper<DdbAssetAccount> {
    DdbAssetAccount selectByZid(String s);
}