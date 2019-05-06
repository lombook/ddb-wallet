package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseDetailRule;
import com.jinglitong.wallet.job.util.MyMapper;

import java.util.List;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseDetailRuleMapper extends MyMapper<FrozenReleaseDetailRule> {
    List<FrozenReleaseDetailRule> selectTodayRules();

    @Update("update frozen_release_detail_rule set  detail_status = #{detailStatus} where detail_id = #{detailId}")
    int updateByDetailIdSelective(FrozenReleaseDetailRule frozenReleaseDetailRule);

    FrozenReleaseDetailRule selecByDetailId(String detailId);

    List<FrozenReleaseDetailRule> selectByState(int i);

}