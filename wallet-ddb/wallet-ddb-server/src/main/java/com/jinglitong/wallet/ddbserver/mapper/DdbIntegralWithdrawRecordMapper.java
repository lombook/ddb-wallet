package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.view.DdbIntegralWithdrawRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.IntegralWithdrawApplyVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DdbIntegralWithdrawRecordMapper extends MyMapper<DdbIntegralWithdrawRecord> {
    DdbIntegralWithdrawRecord selectByZid(String zid);

    // 查询 提现审核列表
	List<IntegralWithdrawApplyVo> getwithdrawApplyInfo(IntegralWithdrawApplyVo vo);
	//	 查询提现列表
	List<IntegralWithdrawApplyVo> getwithdrawInfo(IntegralWithdrawApplyVo vo);
	
	List<DdbIntegralWithdrawRecordVo> getCustWithDrawRecord(DdbIntegralWithdrawRecordVo vo);
}