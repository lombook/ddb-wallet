package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.LockCoinRecord;
import com.jinglitong.wallet.api.model.logic.LockCoinRecordCustomer;
import com.jinglitong.wallet.api.model.logic.UnLockVo;
import com.jinglitong.wallet.api.model.view.LockCoinToltalVo;
import com.jinglitong.wallet.api.model.view.LockSelCoinRecordVO;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface LockCoinRecordMapper extends MyMapper<LockCoinRecord> {
    List<LockCoinRecordCustomer> recordListByChainCoin(LockSelCoinRecordVO coinRecordVo);
    
    List<LockCoinToltalVo> recordToltal(LockCoinToltalVo vo);
    
    List<LockCoinRecordCustomer> recordToltalDetail(LockCoinToltalVo vo);
    
    int countByDate(@Param("lockEndDate") String lockEndDate);
    
    List<UnLockVo> selectByDate(@Param("lockEndDate") String lockEndDate);
    
    int updateStatusById(LockCoinRecord record);
}