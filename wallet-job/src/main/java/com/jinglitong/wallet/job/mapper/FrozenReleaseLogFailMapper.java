package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseLogFail;
import com.jinglitong.wallet.job.util.MyMapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseLogFailMapper extends MyMapper<FrozenReleaseLogFail> {
    List<FrozenReleaseLogFail> selectBycreateTime(String nowTime);

    List<FrozenReleaseLogFail> selectByWalletIdAndRuleId(String walletId, String ruleId);

    List<FrozenReleaseLogFail> selectbetween(String begainTime, String endTime);

    List<FrozenReleaseLogFail> selectBycount();
    @Delete(value = "delete from frozen_release_log_fail where log_id = #{id}")
    void deleteBylogId(String logId);
    
    List<FrozenReleaseLogFail> selectByCountMoreThree();
    List<FrozenReleaseLogFail> selectByCountLessThree();
    @Update("update frozen_release_log_fail set COUNT = COUNT + 1,update_time=now() where zid = #{zid}")
    void updateCountByZid(String zid);
    @Update("update frozen_release_log_fail set success_state = #{successState},success_time=now() where zid = #{zid}")
    void updateSuccessStateByZid(@Param("successState") int successState,@Param("zid") String zid);
    @Delete(value = "delete from frozen_release_log_fail where success_state =1 and success_time <= #{days}")
    void deleteBySuccessTime(String days);
    
    
}