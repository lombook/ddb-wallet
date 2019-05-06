package com.jinglitong.wallet.give.mapper;


import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.MainChainVO;
import com.jinglitong.wallet.give.util.MyMapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MainChainMapper extends MyMapper<MainChain> {

    @Select("SELECT chain_id,chain_name,chain_currency,chain_currency_img,handle_name FROM main_chain where state=1 and app_id =#{appId}")
    @Results({
            @Result(property = "chainId",  column = "chain_id"),
            @Result(property = "chainName", column = "chain_name"),
            @Result(property = "chainCurrency", column = "chain_currency"),
            @Result(property = "handleName", column = "handle_name"),
            @Result(property = "chainCurrencyImg", column = "chain_currency_img")
    })
    List<MainChain> getChainAll(BalanceVO vo);

    @Select("SELECT COUNT(*) FROM main_chain")
    Integer getCount();

    List<Map> getMainChains( MainChainVO mainChainVO);

    @Select("SELECT chain_id,chain_name,handle_name FROM main_chain where state = 1")
    @Results({
            @Result(property = "chainId",  column = "chain_id"),
            @Result(property = "chainName", column = "chain_name"),
            @Result(property = "handleName", column = "handle_name")
    })
    List<HashMap<String,Object>> gethandleNames();
    
    @Select("SELECT chain_id,chain_name,handle_name FROM main_chain where state = 1 and app_id =#{appId}")
    @Results({
            @Result(property = "chainId",  column = "chain_id"),
            @Result(property = "chainName", column = "chain_name"),
            @Result(property = "handleName", column = "handle_name")
    })
    List<HashMap<String,Object>> getmainChainByAppId(MainChainVO mainChainVO);
    
    @Select("SELECT chain_id,chain_name,chain_currency,chain_currency_img FROM main_chain where chain_name = #{chainName}")
    @Results({
            @Result(property = "chainId",  column = "chain_id"),
            @Result(property = "chainName", column = "chain_name"),
            @Result(property = "chainCurrency", column = "chain_currency"),
            @Result(property = "chainCurrencyImg", column = "chain_currency_img")
    })
    MainChain checkChainName(String chainName);

    MainChain getMainChainById(@Param("chainId")String chainId);
    
    @Select("select chain_id,chain_name from main_chain where state=#{state} " )
    @Results({
    	@Result(property="chainId",column="chain_id"),
    	@Result(property="chainName",column="chain_name")
    })
    List<MainChain> getMainChainInfo(boolean state);

    Integer selectCounts(Customer cus);

    List<MainChain> selectAllMainChain(MainChainVO mainChainVO);
    
    Integer updateByChainId(MainChainVO mainChainVO);

    MainChain selectByChainId(String chainId);

	int checksMainChain(MainChainVO mainChainVO);
}