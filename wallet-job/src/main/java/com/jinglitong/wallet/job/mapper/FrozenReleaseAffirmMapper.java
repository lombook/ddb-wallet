package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.api.model.FrozenReleaseAffirm;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrozenReleaseAffirmMapper extends MyMapper<FrozenReleaseAffirm> {
    List<FrozenReleaseAffirm> selectBeforTime(String nowTime);

    List<FrozenReleaseAffirm> selectByWalletIdAndDetailId(String walletId, String ruleId);
}