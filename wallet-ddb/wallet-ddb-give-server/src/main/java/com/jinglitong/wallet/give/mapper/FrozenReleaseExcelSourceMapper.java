package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.FrozenReleaseExcelSource;
import com.jinglitong.wallet.api.model.walletVo.FrozenReleaseVO;
import com.jinglitong.wallet.give.util.MyMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FrozenReleaseExcelSourceMapper extends MyMapper<FrozenReleaseExcelSource> {
    int insertBatch(List<FrozenReleaseExcelSource> excBatch);

    List<FrozenReleaseExcelSource> selectByRuleId(FrozenReleaseVO vo);

    FrozenReleaseExcelSource selectBySourceId(String sourceId);

    @Update("update frozen_release_excel_source set source_status = #{sourceStatus} where source_id = #{sourceId}")
    int updateBySourdeIdSelective(FrozenReleaseExcelSource frozenReleaseExcelSource);

    Integer selectCountByRuleId(String ruleId);

    FrozenReleaseExcelSource selectSourceByWalletIdRuleId(String walletId, String ruleId);

    FrozenReleaseExcelSource selectByWalletIdDetailId(String logId);
}