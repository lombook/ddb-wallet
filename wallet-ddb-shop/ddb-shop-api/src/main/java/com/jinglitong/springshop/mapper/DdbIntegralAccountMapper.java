package com.jinglitong.springshop.mapper;



import com.jinglitong.springshop.entity.DdbIntegralAccount;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbIntegralAccountMapper extends MyMapper<DdbIntegralAccount> {
    /**
     * 根据总账id查询总账
     * @param accountZid 总账id
     * @return
     */
    DdbIntegralAccount selectByZid(@Param("zid") String accountZid);

    /**
     * 增减总账
     * @param amount
     * @param zid
     * @return
     */
    @Update("update ddb_integral_account set amount = amount + #{amount} where zid = #{zid} and amount + #{amount} >= 0")
    int addInteger(@Param("amount") long amount,@Param("zid") String zid);

    DdbIntegralAccount getIngeralAccount(DdbIntegralAccount integralAccount);

    List<DdbIntegralAccount> selectAllExpireParentIdAndState(int state);
}