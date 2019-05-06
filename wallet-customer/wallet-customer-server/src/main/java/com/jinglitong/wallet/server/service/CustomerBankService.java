package com.jinglitong.wallet.server.service;

import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41112;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41113;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41114;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_41111;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.jinglitong.wallet.api.model.BankDic;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.CustomerBank;
import com.jinglitong.wallet.api.model.view.BankDicVo;
import com.jinglitong.wallet.api.model.view.CustomerAddBankVo;
import com.jinglitong.wallet.api.model.view.CustomerBankVo;
import com.jinglitong.wallet.server.mapper.BankDicMapper;
import com.jinglitong.wallet.server.mapper.CustomerBankMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.server.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CustomerBankService {

    @Autowired
    private CustomerBankMapper customerBankMapper;
    @Autowired
    private BankDicMapper bankDicMapper;


 
    /**
     * 查询用户银行卡信息
     * @return
     */

    public CustomerBankVo getCustomerBankInfo() {
		// TODO Auto-generated method stub
    	
    	Customer cus = (Customer) SessionUtil.getUserInfo();
    	CustomerBank customerBank = new CustomerBank();
    	customerBank.setCustId(cus.getCustId());
    	customerBank.setFlag(true);
    	customerBank = customerBankMapper.selectOne(customerBank);
    	CustomerBankVo customerBankVo = null;
    	if(customerBank != null) {
    		customerBankVo = new CustomerBankVo();
    		customerBankVo.setBankName(customerBank.getBankName()==null?"":customerBank.getBankName());
    		String cardNo = customerBank.getCardNo();
    		if(StringUtil.isNotEmpty(cardNo)) {
    			cardNo = cardNoReplaceWithStar(cardNo);
    		}
    		customerBankVo.setCardNo(cardNo==null?"":cardNo);
    		customerBankVo.setImgUrl(customerBank.getImgUrl()==null?"":customerBank.getImgUrl());
    		customerBankVo.setZid(customerBank.getZid()==null?"":customerBank.getZid());
    	}
		return customerBankVo;
	}
    public String cardNoReplaceWithStar(String cardNo) {

        if (cardNo.isEmpty() || cardNo == null) {
            return null;
        } else {
            return replaceWithStar(cardNo, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }
    private String replaceWithStar(String cardNo, String regular) {
        return cardNo.replaceAll(regular, "*");
    }
    /**
     * 解绑银行卡信息
     * @param zid
     * @return 
     */
    public int deleteCustomerBankInfoByZid(String zid) {
		// TODO Auto-generated method stub
    	int deleteByZid = customerBankMapper.deleteByZid(zid);
    	return deleteByZid;
	}
    
    /**
     * 得到所有的银行卡
     * @return
     */
    public List<BankDicVo> getAllBankInfo() {
		// TODO Auto-generated method stub
    	List<BankDic> selectAll = bankDicMapper.selectAll();
    	List<BankDicVo> bankDicVoList = new ArrayList<BankDicVo>();
    	if(selectAll != null && selectAll.size() > 0) {
    		for(BankDic bankDic : selectAll) {
    			BankDicVo bankDicVo = new BankDicVo();
    			bankDicVo.setBankName(bankDic.getBankName()==null?"":bankDic.getBankName());
    			bankDicVo.setSelUrl(bankDic.getSelUrl()==null?"":bankDic.getSelUrl());
    			bankDicVo.setShowUrl(bankDic.getShowUrl()==null?"":bankDic.getShowUrl());
//    			bankDicVo.setZid(bankDicVo.getZid()==null?"":bankDicVo.getZid());
    			bankDicVoList.add(bankDicVo);
    		}
    	}
    	
		return bankDicVoList;
	}
    
    /**
     * 绑定用户银行卡
     * @param vo 
     * @throws Exception 
     */
    public int addCustomerBankInfo(CustomerAddBankVo vo) throws Exception {
		// TODO Auto-generated method stub
    	Customer cus = (Customer) SessionUtil.getUserInfo();
    	//首先验证 银行卡是否全是数字
    	boolean strIsOrNotNumer = StrIsOrNotNumer(vo.getCardNo().trim());
    	if(!strIsOrNotNumer) {// 说明有非数字  有空格
    		throw new Exception(ERROR_41113.getCode() + "");
    	}
    	
    	String cardholder = vo.getCardholder();
    	if(StringUtil.isEmpty(cardholder)) {
    		throw new Exception(ERROR_41111.getCode() + "");
    	}
    	
    	// 首先查询 数据库里有没有已绑定的当前银行卡   一个用户只能绑定一张
    	CustomerBank customerBank = new CustomerBank();
    	String cardNo = vo.getCardNo();
//    	customerBank.setCardNo(cardNo);
    	customerBank.setFlag(true);
    	customerBank.setCustId(cus.getCustId());
    	int selectCount = customerBankMapper.selectCount(customerBank);
    	if(selectCount > 0) {
    		throw new Exception(ERROR_41114.getCode() + "");
    	}
    	
    	customerBank = new CustomerBank();
//    	String cardNo = vo.getCardNo();
    	customerBank.setCardNo(cardNo);
    	customerBank.setFlag(true);
    	
    	selectCount = customerBankMapper.selectCount(customerBank);
    	if(selectCount > 0) {
    		throw new Exception(ERROR_41112.getCode() + "");
    	}
    	customerBank = new CustomerBank();
    	customerBank.setZid(UuidUtil.getUUID());
    	customerBank.setCustId(cus.getCustId());
    	customerBank.setBankName(vo.getBankName());
    	customerBank.setImgUrl(vo.getShowUrl());
    	customerBank.setActiveAddress(vo.getActiveAddress());
    	customerBank.setCardholder(vo.getCardholder());
    	customerBank.setCardNo(cardNo);
    	customerBank.setFlag(true);
    	String dateTime = DateUtils.getDateTime();
    	customerBank.setCreateTime(dateTime);
    	customerBank.setUpdateTime(dateTime);
    	int insert = customerBankMapper.insert(customerBank);
		return insert;
	}
    
    public boolean StrIsOrNotNumer(String str) {
        try {
        	BigDecimal num = new BigDecimal(str);
        } catch (Exception e) {
            return false;//有异常， 说明包含非数字:空格，字母，符号。
        }
        return true;
    }
}
