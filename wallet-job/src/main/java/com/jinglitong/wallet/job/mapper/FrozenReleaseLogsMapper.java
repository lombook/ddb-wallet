package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseLogs;
import com.jinglitong.wallet.job.util.MyMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrozenReleaseLogsMapper extends MyMapper<FrozenReleaseLogs> {
    List<FrozenReleaseLogs> selectByWRid(String walletId, String ruleId);

    FrozenReleaseLogs selectByLogId(String logId);

    List<FrozenReleaseLogs> selectByDetailIdAndTrue(String key);
    @Update("update frozen_release_logs set log_status = #{logStatus},update_time=now(),pay_hash = #{paymentHash} where log_id = #{logId}")
    void updateLogStatusByLogId(@Param("logStatus") int logStatus,@Param("paymentHash") String paymentHash,@Param("logId") String logId);
}