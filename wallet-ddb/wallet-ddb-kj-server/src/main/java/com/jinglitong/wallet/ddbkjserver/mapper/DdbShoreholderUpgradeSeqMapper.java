package com.jinglitong.wallet.ddbkjserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbShoreholderUpgradeSeq;
import com.jinglitong.wallet.ddbkjserver.util.MyMapper;

public interface DdbShoreholderUpgradeSeqMapper extends MyMapper<DdbShoreholderUpgradeSeq> {
	/**
	 * 保存股东升级流水
	 * @param ddbShoreholderUpgradeSeq
	 * @return
	 */
	int saveDdbShoreholderUpgradeSeq(DdbShoreholderUpgradeSeq ddbShoreholderUpgradeSeq);
}