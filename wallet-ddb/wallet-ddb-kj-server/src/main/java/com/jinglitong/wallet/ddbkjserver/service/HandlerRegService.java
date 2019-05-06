package com.jinglitong.wallet.ddbkjserver.service;

import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.CustomerMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HandlerRegService {
	@Autowired
	private CustomerMapper customerMapper;
	
    public void handlerReg(KJNotice notice){
        log.debug("reg customer");
        //查询customer，根据notice中的custId找到该用户的直接上级邀请人，获得邀请人custId。
        String custId = notice.getCustId();
        // 邀请人的custId 
        String inviterCustId = customerMapper.getinviterByinviteeCustId(notice.getCustId());
        
        //将notice中的custId，添加到用户缓存信息CustomerService.CUSTOMER_TREE_DATA中
//        CustomerService.CUSTOMER_TREE_DATA.get(custId);
        CustomerInfo4LevelDiff CustomerInfo4LevelDiff = new CustomerInfo4LevelDiff();
        // 组装 CustomerInfo4LevelDiff 信息   只存放前四个属性
        CustomerInfo4LevelDiff.setUserid(custId);
        // 得到上级的层级
        int treeNodeLevel = ConstantDict.CUSTOMER_TREE_DATA.get(inviterCustId).getTreeNodeLevel();
        CustomerInfo4LevelDiff.setTreeNodeLevel(treeNodeLevel + 1);// 需要获得上级的层级，然后 加 1
        // 上级的上级用户列表
        List<String> parentsList = ConstantDict.CUSTOMER_TREE_DATA.get(inviterCustId).getParentsList();
        List<String> currenctList = new ArrayList<String>();
        currenctList.addAll(parentsList);
        //添加当前级别
        currenctList.add(inviterCustId);
        CustomerInfo4LevelDiff.setParentsList(currenctList);// 上级用户列表 ，需要获得上级的上级用户列表  ， 然后 在加上上级即可
        CustomerInfo4LevelDiff.setDirectChildrenList(null);//直接下级用户列表   新注册用户，为null
        
        ConstantDict.CUSTOMER_TREE_DATA.put(custId,CustomerInfo4LevelDiff);
        
    }
}
