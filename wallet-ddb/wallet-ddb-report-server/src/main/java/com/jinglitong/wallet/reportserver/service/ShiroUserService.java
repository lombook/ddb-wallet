package com.jinglitong.wallet.reportserver.service;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.reportserver.mapper.AdminMapper;
import com.jinglitong.wallet.reportserver.mapper.CustomerMapper;
import com.jinglitong.wallet.reportserver.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Set;

@Service
public class ShiroUserService {

    @Autowired
    public AdminMapper adminMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private CustomerMapper customerMapper;

    public Admin findByUsername(String userName, String appId) {
        Weekend<Admin> weekend = Weekend.of(Admin.class);
        WeekendCriteria<Admin, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Admin::getUsername, userName);
        criteria.andEqualTo(Admin::getAppId, appId);
        return adminMapper.selectOneByExample(weekend);
    }


    public Set<String> getAllMenuCode() {
        return menuMapper.getALLMenuCode();
    }

    public Set<String> findMenuCodeByUserId(String adminId) {
        return menuMapper.findMenuCodeByUserId(adminId);
    }

    /**
     * 账号和appId 查询账号
     * @param account
     * @param appId
     * @return
     */
    public Customer findByAccount(String account,String appId) {
        Weekend<Customer> weekend = Weekend.of(Customer.class);
        WeekendCriteria<Customer, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Customer::getAccount,account);
        criteria.andEqualTo(Customer::getAppId,appId);
        return customerMapper.selectOneByExample(weekend);
    }
}
