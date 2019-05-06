package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRule;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRuleVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DdbIntegralWithdrawRuleMapper extends MyMapper<DdbIntegralWithdrawRule> {
    DdbIntegralWithdrawRule selectByZid(String withdrawRuleId);
    
    DdbIntegralWithdrawRuleVo getRuleByName(String ruleName) ;
    
    DdbIntegralWithdrawRuleVo getRuleByZid(String zid) ;
}