package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseRule;
import com.jinglitong.wallet.api.model.logic.ReleaseCurrencyVo;
import com.jinglitong.wallet.api.model.walletVo.FrozenReleaseVO;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FrozenReleaseRuleMapper extends MyMapper<FrozenReleaseRule> {
    List<Map> getFRList(FrozenReleaseVO vo);
    FrozenReleaseRule getById(FrozenReleaseVO vo);

    List<ReleaseCurrencyVo> selectBy(String voAccount, String account, String appId);

    @Update("update frozen_release_rule set fr_status = #{frStatus} where rule_id = #{ruleId} ")
    int updateByRuleIdSelective(FrozenReleaseRule frozenReleaseRule);

    FrozenReleaseRule selectByRuleId(String ruleId);
    List<Map> qryRule(String ruleName, String appId);


}