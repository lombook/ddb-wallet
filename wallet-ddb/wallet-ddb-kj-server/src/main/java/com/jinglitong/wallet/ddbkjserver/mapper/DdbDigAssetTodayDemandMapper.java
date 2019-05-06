package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbDigAssetTodayDemand;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbDigAssetTodayDemandMapper extends MyMapper<DdbDigAssetTodayDemand> {
    List<DdbDigAssetTodayDemand> selectByCustId(Customer customer);
}