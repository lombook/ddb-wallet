package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbShouldFrozen;
import com.jinglitong.wallet.give.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DdbShouldFrozenMapper extends MyMapper<DdbShouldFrozen> {

    List<DdbShouldFrozen> selectByExcutime();

    @Update("update ddb_should_frozen set frozen_days= #{todaySumAmount} where  zid = #{shouldTransferId}")
    void updateTodayFrozenAmountByzid(@Param("todaySumAmount") Long todaySumAmount, @Param("shouldTransferId") String shouldTransferId);

    @Update("update ddb_should_frozen set frozen_days= frozen_days+1,left_amount = left_amount -rfrozen_amount,update_time = now(),excute_time =#{dateTime}  where  zid = #{shouldTransferId}")
    void updateFrozenDaysAndleftAmountByshouId(@Param("dateTime") String dateTime, @Param("shouldTransferId") String shouldTransferId);

    List<DdbShouldFrozen> selectByExcutimeBeforeToday();

    @Update("update ddb_should_frozen set frozen_days= frozen_days+1,left_amount = 0,update_time = now(),excute_time =#{dateTime}  where  zid = #{shouldTransferId}")
    void updateFrozenDaysAndleftAmounttoZeroByshouId(@Param("dateTime") String dateTime, @Param("shouldTransferId") String shouldTransferId);

    DdbShouldFrozen selectByShouldTransferId(String shouldTransferId);
}