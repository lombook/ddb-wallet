package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.give.util.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public interface DdbConsumerHistoryMapper extends MyMapper<DdbConsumerHistory> {
    DdbConsumerHistory checkMessageId(String msgID);
}