package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbConsumerHistory;
import com.jinglitong.wallet.ddbserver.util.MyMapper;


public interface DdbConsumerHistoryMapper extends MyMapper<DdbConsumerHistory> {
	 DdbConsumerHistory checkMessageId(String msgID);
}