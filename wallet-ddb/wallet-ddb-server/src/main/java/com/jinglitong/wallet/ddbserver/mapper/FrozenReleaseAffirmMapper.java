package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.api.model.FrozenReleaseAffirm;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseAffirmMapper extends MyMapper<FrozenReleaseAffirm> {
    List<FrozenReleaseAffirm> selectBeforTime(String nowTime);

    List<FrozenReleaseAffirm> selectByWalletIdAndDetailId(String walletId, String ruleId);
}