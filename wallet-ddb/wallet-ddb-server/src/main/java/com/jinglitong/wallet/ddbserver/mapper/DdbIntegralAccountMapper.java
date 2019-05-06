package com.jinglitong.wallet.ddbserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbIntegralAccountMapper extends MyMapper<DdbIntegralAccount> {

	int addBf(@Param("amount") int amount,@Param("zid") String zid);

	DdbIntegralAccount selectByRealName(String realName);

	int addInteger(@Param("amount")int amount,@Param("zid") String zd);


	List<DdbIntegralAccount> selectAllExpireParentIdAndState(int state);

    DdbIntegralAccount selectParentAByInte(String inteId);

	DdbIntegralAccount selectByZid(String inteId);
}