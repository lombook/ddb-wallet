package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.job.util.MyMapper;

public interface DdbAssetAccountMapper extends MyMapper<DdbAssetAccount> {
    DdbAssetAccount selectByZid(String s);
}