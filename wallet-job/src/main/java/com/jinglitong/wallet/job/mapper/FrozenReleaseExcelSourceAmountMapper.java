package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseExcelSourceAmount;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrozenReleaseExcelSourceAmountMapper extends MyMapper<FrozenReleaseExcelSourceAmount> {
    int insertBatch(List<FrozenReleaseExcelSourceAmount> excBatchA);

    FrozenReleaseExcelSourceAmount selectBySourceId(String sourceId);

    Integer updateByprivateAndupdateTime(String s, String dateTime, String id);
}