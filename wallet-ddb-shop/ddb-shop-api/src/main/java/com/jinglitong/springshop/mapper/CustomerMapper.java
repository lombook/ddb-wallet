package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.CustVO;
import com.jinglitong.springshop.vo.response.CustomerOrderVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CustomerMapper extends MyMapper<Customer> {



    /**
     * 修改用户认证状态
     * @param state
     * @param custId
     */
    @Update("update customer set certificate_state = #{state} where cust_id = #{custId}")
    Integer updateCertificateStateByzid(@Param("state") int state, @Param("custId") String custId);

    List<Map<String,String>> selectinviteList(@Param("zid") String zid);

    List<Customer> selectCustList(CustVO custVO);

    Customer getByCustId(Customer customer);

    /**
     * 获取直接邀请人
     * @param customer
     * @return
     */
    Customer getDircetInviter(Customer customer);

    /**
     * 获取用户邀请关系List
     * @param customer
     * @return
     */
    List<HashMap<String ,Object>> getInviteList(Customer customer);

    Customer selectByCustIdOrInviteCode(@Param("custId") String custId, @Param("inviteCode") String inviteCode);

    CustomerOrderVO selectBySubOrderId(@Param("subOrderId") String subOrderId);
}