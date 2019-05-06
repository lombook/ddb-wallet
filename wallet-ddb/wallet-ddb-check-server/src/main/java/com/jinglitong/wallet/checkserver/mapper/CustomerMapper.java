package com.jinglitong.wallet.checkserver.mapper;

import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.Country;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.checkserver.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Service
public interface CustomerMapper extends MyMapper<Customer> {

    List<CustomerAndCountry> getCustomerList(CustSelVO custSelVO);

    Country selectById(String admin_id);

    BasicCustomerInfo getBasicCustomerInfo(Map map);

    void updCustomerInfo(Map map);

    Wallet selectByCusId(String id);
    
    //根据邀请码和appId查询用户
    Customer getCustByCode(String code,String appId);
    //根据邀请码查询用户
    Customer getTheCustByCode(String code);
    
    //根据邀请码查询被邀请的用户列表
    List<Customer> getInvitedCustomers(String code);
    
    //根据邀请人userID获取被邀请的用户列表
    List<String> getInvitorsByInviteUserId(@Param("userId") String userId);
    
  //根据邀请码查询被邀请的用户列表
    List<CustomerAndCountry> getInvitors(CustSelVO custSelVO);
    CustomerAndCountry getCustomerInfo(String id);

    Customer selectByUsername(@Param("account")String account,@Param("appId")String appId);

    Integer selectCounts(Customer cus);

    Customer selectBySelCode(Customer customer);

   Integer updateByCustId(Customer customer);

    Customer selectByCustId(String custId);
    // 根据当前用户  查询 直接邀请人
    String getinviterByinviteeCustId(String custId);
  //根据订单用户的CustId获取其直接下级用户的CustId
    List<String> getinviteeByOrderUserCustId(@Param("userId") String userId);
    
    //查询用户列表中所有用户的user_id
    List<String> getAllUserId();
    
    /**
     * 根据frule_id和flow_id和custmer_id以及state从分红拆单表中判断该用户是否存在加权分红
     * @param flow_id
     * @param cust_id
     * @param frule_id
     * @param state
     * @return
     */
    Integer selectOrderCount(@Param("flow_id")String flow_id, @Param("cust_id")String cust_id, @Param("frule_id")String frule_id, @Param("state")int state);
}