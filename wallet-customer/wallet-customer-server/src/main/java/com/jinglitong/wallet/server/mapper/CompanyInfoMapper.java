package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.CompanyInfo;
import com.jinglitong.wallet.server.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface CompanyInfoMapper extends MyMapper<CompanyInfo> {
    void companyInfoMapper(String companyName);
}