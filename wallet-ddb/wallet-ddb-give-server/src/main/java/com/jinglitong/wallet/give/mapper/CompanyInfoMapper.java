package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.CompanyInfo;
import com.jinglitong.wallet.give.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface CompanyInfoMapper extends MyMapper<CompanyInfo> {
    void companyInfoMapper(String companyName);
}