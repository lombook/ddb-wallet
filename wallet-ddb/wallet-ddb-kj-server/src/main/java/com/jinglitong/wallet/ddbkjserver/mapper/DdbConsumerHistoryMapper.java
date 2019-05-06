package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

import org.springframework.stereotype.Component;

@Component
public interface DdbConsumerHistoryMapper extends MyMapper<DdbConsumerHistory> {
    DdbConsumerHistory checkMessageId(String msgID);
}