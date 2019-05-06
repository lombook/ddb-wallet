package com.jinglitong.wallet.sendserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.sendserver.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DdbConsumerHistoryMapper extends MyMapper<DdbConsumerHistory> {
    DdbConsumerHistory selectByMessId(String msgId);
}