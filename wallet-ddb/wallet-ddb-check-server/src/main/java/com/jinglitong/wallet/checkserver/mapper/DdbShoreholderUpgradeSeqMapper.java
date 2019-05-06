package com.jinglitong.wallet.checkserver.mapper;

import com.jinglitong.wallet.checkserver.util.MyMapper;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholderUpgradeSeq;

public interface DdbShoreholderUpgradeSeqMapper extends MyMapper<DdbShoreholderUpgradeSeq> {
	/**
	 * 保存股东升级流水
	 * @param ddbShoreholderUpgradeSeq
	 * @return
	 */
	int saveDdbShoreholderUpgradeSeq(DdbShoreholderUpgradeSeq ddbShoreholderUpgradeSeq);
}