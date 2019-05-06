package com.jinglitong.wallet.reportserver.mapper;

import org.springframework.stereotype.Component;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.reportserver.util.MyMapper;

@Component
public interface DdbConsumerHistoryMapper extends MyMapper<DdbConsumerHistory> {
    DdbConsumerHistory checkMessageId(String msgID);
}