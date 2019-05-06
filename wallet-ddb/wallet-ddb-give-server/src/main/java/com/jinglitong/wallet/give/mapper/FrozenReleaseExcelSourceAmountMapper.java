package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseExcelSourceAmount;
import com.jinglitong.wallet.give.util.MyMapper;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface FrozenReleaseExcelSourceAmountMapper extends MyMapper<FrozenReleaseExcelSourceAmount> {
    int insertBatch(List<FrozenReleaseExcelSourceAmount> excBatchA);

    FrozenReleaseExcelSourceAmount selectBySourceId(String sourceId);

    Integer updateByprivateAndupdateTime(String s, String dateTime, String id);
}