package com.jinglitong.wallet.job.mapper;

import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.Country;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustSelVO;

import com.jinglitong.wallet.job.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerMapper extends MyMapper<Customer> {

    List<CustomerAndCountry> getCustomerList(CustSelVO custSelVO);

    Country selectById(String admin_id);

    BasicCustomerInfo getBasicCustomerInfo(Map map);

    void updCustomerInfo(Map map);

    Wallet selectByCusId(String id);
    
    //根据邀请码和appId查询用户
    Customer getCustByCode(String code, String appId);
    //根据邀请码查询用户
    Customer getTheCustByCode(String code);

    //根据邀请码查询被邀请的用户列表
    List<Customer> getInvitedCustomers(String code);

  //根据邀请码查询被邀请的用户列表
    List<CustomerAndCountry> getInvitors(CustSelVO custSelVO);
    CustomerAndCountry getCustomerInfo(String id);

    Customer selectByUsername(@Param("account") String account, @Param("appId") String appId);

    Integer selectCounts(Customer cus);

    Customer selectBySelCode(Customer customer);

   Integer updateByCustId(Customer customer);

    Customer selectByCustId(String custId);
    
    int selectCountByDate(@Param("appId") String appId,@Param("yesTime") String yesTime,@Param("toTime") String toTime);
}