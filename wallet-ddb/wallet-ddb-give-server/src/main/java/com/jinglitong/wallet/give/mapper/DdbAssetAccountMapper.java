package com.jinglitong.wallet.give.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.give.util.MyMapper;

public interface DdbAssetAccountMapper extends MyMapper<DdbAssetAccount> {
    DdbAssetAccount selectByZid(String s);
}