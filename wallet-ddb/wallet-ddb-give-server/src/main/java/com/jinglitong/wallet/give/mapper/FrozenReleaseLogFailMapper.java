package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseLogFail;
import com.jinglitong.wallet.give.util.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseLogFailMapper extends MyMapper<FrozenReleaseLogFail> {
    List<FrozenReleaseLogFail> selectBycreateTime(String nowTime);

    List<FrozenReleaseLogFail> selectByWalletIdAndRuleId(String walletId, String ruleId);

    List<FrozenReleaseLogFail> selectbetween(String begainTime, String endTime);

    List<FrozenReleaseLogFail> selectBycount();
    @Delete(value = "delete from frozen_release_log_fail where log_id = #{id}")
    void deleteBylogId(String logId);
}