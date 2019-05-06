package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.ddbapi.model.DdbCustOwnTree;
import com.jinglitong.wallet.ddbapi.model.view.DdbCustOwnTreeVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public interface DdbCustOwnTreeMapper extends MyMapper<DdbCustOwnTree> {
    DdbCustOwnTree selectByCustId(String custId);

    @Update("update ddb_cust_own_tree  set tree_num = tree_num + #{treeNum} where cust_id = #{custId} and (tree_num + #{treeNum}) >= 0")
    Integer updateTreeNumBycustId(@Param("treeNum") Integer treeNumInteger,@Param("custId") String custId);

    ArrayList<DdbCustOwnTreeVO> selectByAccountList(DdbCustOwnTreeVO treeVo);
}