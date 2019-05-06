package com.jinglitong.wallet.sendserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbSendRecord;
import com.jinglitong.wallet.sendserver.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbSendRecordMapper extends MyMapper<DdbSendRecord> {
    List<DdbSendRecord> selectByExcuTime();
}