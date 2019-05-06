package com.jinglitong.springshop.servcie;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.service.RedisService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/9 17:26
 * @Version 1.0
 **/
@Service
@Transactional
public class CustomerService {
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private RedisService redisService;

    public LoginVo getCustomerInfo(String userName){
        Customer customer = (Customer) redisService.getObject(userName);
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(customer,loginVo);
        loginVo.setZid(customer.getCustId());
        return loginVo;
    }

    public Customer getCustomer(String zid) {
    	Customer cust = new Customer();
    	cust.setCustId(zid);
        return customerMapper.getByCustId(cust);
    }

    public Integer updateCustomer(Customer customer) {
        Customer cust = customerMapper.getByCustId(customer);
        customer.setId(cust.getId());
        return customerMapper.updateByPrimaryKeySelective(customer);
    }

    public Map inviteList(String zid,PageVo page) {
        if (page.getPageNum() != null && page.getPageSize() != null) {
            PageHelper.startPage(page.getPageNum(), page.getPageSize());
        }
        List<Map<String, String>> inviteList = customerMapper.selectinviteList(zid);
        PageInfo pageinfo = new PageInfo(inviteList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("inviteList", inviteList);
        return map;
    }

    public Map<String,Object> getCustomerList(CustVO custVO) {
        if (custVO.getPageNum() != null && custVO.getPageSize() != null) {
            PageHelper.startPage(custVO.getPageNum(), custVO.getPageSize());
        }
        List<Customer> customerList = customerMapper.selectCustList(custVO);
        PageInfo pageinfo = new PageInfo(customerList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("custList", customerList);
        return map;
    }

    public Integer updateCustomerOrIdentyIfo(CustomerVo customerVo) {
        Customer customer = new Customer();
        if(customerVo.getId() != null){
            customer.setId(customerVo.getId());
        }else {
            return 0;
        }
        if(customerVo.getState() != null){
            customer.setState(customerVo.getState());
        }
        if(customerVo.getRemark() != null){
            customer.setRemark(customerVo.getRemark());
        }
        if(customerVo.getEmail() != null){
            customer.setEmail(customerVo.getEmail());
        }
        int update = customerMapper.updateByPrimaryKeySelective(customer);

        return update;
    }


    /*public Map<String,Object> getTaskIntegerRecord(String custId,InteCustRecordVo inteCustRecordVo) {
        if (inteCustRecordVo.getPageNum() != null && inteCustRecordVo.getPageSize() != null) {
            PageHelper.startPage(inteCustRecordVo.getPageNum(), inteCustRecordVo.getPageSize());
        }
        List<IntegralCustRecord> integralCustRecordList = integralCustRecordMapper.selectByCustIdAndIntegName(custId,inteCustRecordVo.getInteName());
        PageInfo pageinfo = new PageInfo();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", pageinfo.getTotal());
        map.put("custList", integralCustRecordList);
        return map;
    }*/
}
