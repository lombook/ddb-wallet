package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.InvestAccount;
import com.jinglitong.wallet.api.model.view.InvestAccountVO;
import com.jinglitong.wallet.job.util.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface InvestAccountMapper extends MyMapper<InvestAccount> {
	List<InvestAccount> qryInvestAccounts(InvestAccountVO vo);
}