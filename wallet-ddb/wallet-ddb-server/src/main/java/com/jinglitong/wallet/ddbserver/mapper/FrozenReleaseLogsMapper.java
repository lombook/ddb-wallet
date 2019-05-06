package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseLogs;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseLogsMapper extends MyMapper<FrozenReleaseLogs> {
    List<FrozenReleaseLogs> selectByWRid(String walletId, String ruleId);

    FrozenReleaseLogs selectByLogId(String logId);

    List<FrozenReleaseLogs> selectByDetailIdAndTrue(String key);
}