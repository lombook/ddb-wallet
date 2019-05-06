package com.jinglitong.wallet.server.mapper;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.CustomerBank;
import com.jinglitong.wallet.server.util.MyMapper;
@Service
public interface CustomerBankMapper extends MyMapper<CustomerBank>{
    int deleteByZid(String zid);


}