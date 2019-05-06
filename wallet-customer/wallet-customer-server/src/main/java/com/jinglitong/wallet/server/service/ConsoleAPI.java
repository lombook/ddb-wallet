package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.server.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.server.mapper.LockCoinRuleMapper;
import com.jinglitong.wallet.server.mapper.MainChainMapper;
import com.jinglitong.wallet.server.mapper.WalletMapper;
import com.jinglitong.wallet.api.model.LockCoinRule;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.logic.LBallanceVO;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.LockSelCoinRecordVO;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by fan on 208/6/8.
 */
@Service
public class ConsoleAPI {
    @Autowired
    MainChainMapper mainChainMapper;

    @Autowired
    WalletMapper walletMapper;

    @Autowired
    LockCoinRuleMapper lockCoinRuleMapper;
    public Map<String,Object> balances(LockSelCoinRecordVO coinRecordVo) {
        LockCoinRule lockCoinRule = lockCoinRuleMapper.selectByPrimaryKey(coinRecordVo.getLockRecordId());
        BalanceVO balanceVO = new BalanceVO();
        ChainService service;
        if(lockCoinRule != null){
            balanceVO.setChainId(lockCoinRule.getChainId());
            balanceVO.setAddress(coinRecordVo.getWalletAddress());
            service = getChainService(lockCoinRule.getChainId());
        }else {
            service = getChainService(null);
        }

        LBallanceVO res = service.getBallance(balanceVO);
        if(res.getResCode().equals(0)){
            return JsonUtil.toJsonSuccess("查询成功",res.getDataList());
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
    }

    private ChainService getChainService(String chainId) {
        MainChain mainChain = mainChainMapper.selectByPrimaryKey(chainId);
        if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) )
            return null;
        ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
        return chainService;

    }
}
