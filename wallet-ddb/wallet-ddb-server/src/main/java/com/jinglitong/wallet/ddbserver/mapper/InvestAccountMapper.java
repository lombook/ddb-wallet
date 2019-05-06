package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.InvestAccount;
import com.jinglitong.wallet.api.model.view.InvestAccountVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

public interface InvestAccountMapper extends MyMapper<InvestAccount> {
	List<InvestAccount> qryInvestAccounts(InvestAccountVO vo);
}