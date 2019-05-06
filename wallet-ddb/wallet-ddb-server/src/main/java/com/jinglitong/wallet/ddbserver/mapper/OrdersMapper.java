package com.jinglitong.wallet.ddbserver.mapper;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.ddbapi.model.Orders;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

@Service
public interface OrdersMapper extends MyMapper<Orders> {
    
}