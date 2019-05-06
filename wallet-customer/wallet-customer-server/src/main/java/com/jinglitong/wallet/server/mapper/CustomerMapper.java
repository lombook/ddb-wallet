package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.BasicCustomerInfo;
import com.jinglitong.wallet.api.model.Country;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.logic.CustomerAndCountry;
import com.jinglitong.wallet.api.model.view.CustSelVO;
import com.jinglitong.wallet.server.util.MyMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface CustomerMapper extends MyMapper<Customer> {

    List<CustomerAndCountry> getCustomerList(CustSelVO custSelVO);
    List<CustomerAndCountry> getCusts(CustSelVO custSelVO);
    List<CustomerAndCountry> getCustsByInvitor(CustSelVO custSelVO);

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
    
  //根据邀请码查询被邀请的用户列表
    List<CustomerAndCountry> getInvitors(CustSelVO custSelVO);
    CustomerAndCountry getCustomerInfo(String id);

    Customer selectByUsername(@Param("account")String account,@Param("appId")String appId);
    List<Customer> selectByUserAccount(@Param("account")String account,@Param("appId")String appId);

    Integer selectCounts(Customer cus);
	int getCustsCountByInvitor(CustSelVO custSelVO);
}