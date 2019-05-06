package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.WalletCoin;
import com.jinglitong.wallet.server.util.MyMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface WalletCoinMapper extends MyMapper<WalletCoin> {
    @Select("select c.currency,c.token_address,c.coin_name,c.coin_remark,c.coin_img from wallet_coin t left join sub_chain c on t.coin_id = c.coin_id where t.wallet_id=#{walletId} and c.state=1 and t.app_id=#{appId}")
    @Results({
            @Result(property = "tokenAddress",  column = "token_address"),
            @Result(property = "coinName",  column = "coin_name"),
            @Result(property = "coinRemark",  column = "coin_remark"),
            @Result(property = "coinImg",  column = "coin_img")
    })
    List<SubChain> getOwnCoinByWalletId(@Param("walletId")String walletId,@Param("appId")String appId);

    @Select("SELECT sub.coin_name,sub.coin_img,sub.currency,sub.coin_remark,"
    +"IFNULL(t2.has , 0) as status,sub.coin_id,sub.coin_img"
    +" FROM sub_chain sub LEFT JOIN(SELECT t.coin_id ,"
	+"'1' AS has FROM wallet_coin t WHERE t.wallet_id = #{walletId} AND t.is_show = 1"
    +") t2 ON sub.coin_id = t2.coin_id WHERE sub.chain_id = #{chainId} and sub.base_chain=0 and sub.app_id =#{appId} order by status desc")
    List<Map> getAllCoinByWalletId(@Param("walletId") String walletId, @Param("chainId") String chainId,@Param("appId") String appId);
}