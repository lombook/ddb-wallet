package com.jinglitong.wallet.reportserver.mapper;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.reportserver.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CustomerMapper extends MyMapper<Customer> {


    List<Map> selectByAccountInviteCodeAndNumber( Map map);

    List<Map<String, Object>> selectOrderAccount(Map<String, String> result);
    List<Map<String, Object>> selectteamActualDividends(Map<String, String> result);
    List<Map<String, Object>> selectRelationshipTree(Map<String, String> result);
    List<Map<String, Object>> selectChildren(@Param("account")String account,@Param("appId")String appId);
    List<Map<String, Object>> selectselfChildren(@Param("account")String account,@Param("appId")String appId);
    String selectCustIdbyOrder(@Param("shopTrade")String shopTrade);
    List<String> selectTestCusts();
    String selectCustNamebyAccount(@Param("appId")String appId,@Param("account")String account);
    List<Map<String, Object>> selectOrders(Map<String, String> result);
    Customer selectByCustId(String custId);
    Customer selectByTreeLevelAndinvitecode(@Param("treeLevel")Integer treeLevel,@Param("inviteCode")String inviteCode,@Param("appId")String appId);
    
}