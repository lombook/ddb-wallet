package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.ddbapi.model.FrozenReleaseMiddlePay;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FrozenReleaseMiddlePayMapper extends MyMapper<FrozenReleaseMiddlePay> {
    FrozenReleaseMiddlePay selectMiddleByDetailIdAndSourceId(@Param("detailId") String detailId,@Param("sourceId") String sourceId);

    List<FrozenReleaseMiddlePay> selectByState();
    @Update("update frozen_release_middle_pay set status = #{status} where  detail_id = #{detailId} and source_id = #{sourceId} ")
    void updateStateByZid(@Param("sourceId") String sourceId,@Param("status") int i,@Param("detailId") String detailId);

    List<FrozenReleaseMiddlePay> selectByWrid(@Param("walletId") String walletId,@Param("ruleId") String ruleId);
}