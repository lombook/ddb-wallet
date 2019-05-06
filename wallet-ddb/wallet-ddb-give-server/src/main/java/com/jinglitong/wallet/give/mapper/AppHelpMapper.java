package com.jinglitong.wallet.give.mapper;


import com.jinglitong.wallet.api.model.AppHelp;
import com.jinglitong.wallet.api.model.view.AppHelpSelVO;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface AppHelpMapper extends MyMapper<AppHelp> {
    @Select("select help_id,title from app_help t where t.state=#{state} and t.app_type=#{appType} and t.app_id=#{appId} order by show_order ,create_time ")
    @Results({
            @Result(property = "helpId",  column = "help_id")
    })
    List<AppHelp> getAppHelpByType(AppHelp vo);

    List<AppHelp> getAppHelps(AppHelpSelVO appHelpSelVO);

    int updateByHelpId(AppHelp vo);
}