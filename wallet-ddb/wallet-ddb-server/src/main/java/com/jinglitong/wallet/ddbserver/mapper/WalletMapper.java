package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface WalletMapper extends MyMapper<Wallet> {

    public List<Map> userwallets(Customer cust);


    List<HashMap<String,Object>> selectChain(@Param("appId")String appId);
    
    void  insertHistory(Wallet wa);
    
    void deleteWalletCoin(String walletId);

    Wallet selectById(String id);

    @Select("SELECT w.* FROM customer t , wallet w WHERE t.account = #{account} AND t.cust_id = w.cust_id AND w.chain_id = #{chainId}")
    @Results({
            @Result(property = "walletId",  column = "wallet_id"),
            @Result(property = "walletName", column = "wallet_name"),
            @Result(property = "custId",  column = "cust_id"),
            @Result(property = "publicKey", column = "public_key"),
            @Result(property = "chainId",  column = "chain_id")
    })
    List<Wallet> getFRByAccount(@Param("account") String account, @Param("chainId") String chainId,@Param("appId") String appId);

    Wallet selectByWalletId(String walletId);

    Wallet selectByCustIdAndAppIdAndChainId(@Param("custId") String custId,@Param("appId") String appId,@Param("chainId") String chainId);
}