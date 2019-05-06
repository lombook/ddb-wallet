package com.jinglitong.wallet.job.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbIntegralAccountMapper extends MyMapper<DdbIntegralAccount> {

	int addBf(@Param("amount") int amount, @Param("zid") String zid);

	DdbIntegralAccount selectByRealName(String realName);

	int addInteger(@Param("amount") int amount, @Param("zid") String zd);


	List<DdbIntegralAccount> selectAllExpireParentIdAndState(int state);
}