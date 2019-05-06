package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.LockCoinRule;
import com.jinglitong.wallet.api.model.logic.LockCoinRuleChainCoin;
import com.jinglitong.wallet.api.model.view.LockConRuleVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface LockCoinRuleMapper extends MyMapper<LockCoinRule> {
    List<LockCoinRuleChainCoin> selectAllList(LockConRuleVO lockCoinRuleVo);
    
    List<LockCoinRule> selectById(Map<String, String> map);
}