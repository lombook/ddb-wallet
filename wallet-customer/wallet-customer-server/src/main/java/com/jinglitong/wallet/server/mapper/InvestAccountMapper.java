package com.jinglitong.wallet.server.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.InvestAccount;
import com.jinglitong.wallet.api.model.view.InvestAccountVO;
import com.jinglitong.wallet.server.util.MyMapper;

public interface InvestAccountMapper extends MyMapper<InvestAccount> {
	List<InvestAccount> qryInvestAccounts(InvestAccountVO vo);
}