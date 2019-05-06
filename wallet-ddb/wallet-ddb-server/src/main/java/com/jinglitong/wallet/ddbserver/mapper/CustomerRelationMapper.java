package com.jinglitong.wallet.ddbserver.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.jinglitong.wallet.ddbapi.model.CustomerRelation;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
@Service
public interface CustomerRelationMapper extends MyMapper<CustomerRelation>{
    
    CustomerRelation selectByuserId(String userId);
    
    CustomerRelation selectByFriendId(String friendId);
    //根据 userid  friendid  查询  
    CustomerRelation selectByUserIdAndFriendId(@Param("userid")String userid,@Param("friendid")String friendid);
    
}