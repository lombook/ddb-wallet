package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.SubChainVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface SubChainMapper extends MyMapper<SubChain> {
    List<Map> getSubChains(SubChainVO subChainVO);

    List<SubChain> selectByChainId(SubChainVO subChainVO);

    SubChain selectById(String id);
    @Update("update sub_chain set state = #{param2} where chain_id = #{param1}")
    Integer updateByMainChainId(String chainId, int i);

    SubChain selectByCoinId(String coinId);
}