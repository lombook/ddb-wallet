package com.jinglitong.wallet.ddbserver.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.CustomerBank;
import com.jinglitong.wallet.api.model.IdentityInfo;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.view.IntegralWithdrawApplyVo;
import com.jinglitong.wallet.ddbapi.model.view.IntegralwithdrawCustomerApplyVo;
import com.jinglitong.wallet.ddbserver.mapper.CustomerBankMapper;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWithdrawRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.IdentityInfoMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;

@Service
public class WithdrawApplyInfoService {

	@Autowired
	private DdbIntegralWithdrawRecordMapper ddbIntegralWithdrawRecordMapper;
	@Autowired
    private CustomerMapper customerMapper;
	@Autowired
    private IdentityInfoMapper identityInfoMapper;
	
	@Autowired
    private CustomerBankMapper customerBankMapper;
	
	@Autowired
    private DdbIntegralWalletMapper ddbIntegralWalletMapper;
	
	
	public HashMap<String, Object> getwithdrawApplyInfo(IntegralWithdrawApplyVo vo) {
		// TODO Auto-generated method stub
		List<IntegralWithdrawApplyVo> withdrawApplyInfo = null;
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		
		if(vo.getFlag()) {// true  提现列表
			String drawStatus = vo.getDrawStatus();
//			if(StringUtil.isEmpty(drawStatus)) {
//				vo.setDrawStatus("0");
//			}
			  
			withdrawApplyInfo = ddbIntegralWithdrawRecordMapper.getwithdrawInfo(vo);
			
		}else {//  false  审核列表
			String applyStatus = vo.getApplyStatus();
//			if(StringUtil.isEmpty(applyStatus)) {
//				vo.setApplyStatus("0");
//			}
			withdrawApplyInfo = ddbIntegralWithdrawRecordMapper.getwithdrawApplyInfo(vo);
		}
		
		PageInfo pageinfo = new PageInfo(withdrawApplyInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("customers",withdrawApplyInfo);
        return  map;
	}



	public IntegralwithdrawCustomerApplyVo getwithdrawDetailInfo(String zid) {
		// TODO Auto-generated method stub
		//首先根据 zid   查询  申请记录表
		DdbIntegralWithdrawRecord ddbIntegralWithdrawRecord = new DdbIntegralWithdrawRecord();
		ddbIntegralWithdrawRecord.setZid(zid);
		ddbIntegralWithdrawRecord = ddbIntegralWithdrawRecordMapper.selectByZid(zid);
		// 申请信息
		// 持卡人  申请人   用户姓名
		String cardholder = ddbIntegralWithdrawRecord.getCardholder();
		// 申请提现钱的数量
		Integer amount = ddbIntegralWithdrawRecord.getAmount();
		// 申请日期
		String applyTime  = ddbIntegralWithdrawRecord.getApplyTime();// 2018-11-26 13:40:15.0
		if(applyTime!=null) {
			applyTime = applyTime.substring(0, applyTime.length()-2);
		}
		// 审批日期
		String auditTime  = ddbIntegralWithdrawRecord.getAuditTime();// 2018-11-26 13:40:15.0
		if(auditTime!=null) {
			auditTime = auditTime.substring(0, auditTime.length()-2);
		}
		// 认证信息
		// 身份证号
		String custId = ddbIntegralWithdrawRecord.getCustId();
//		Customer selectByCustId = customerMapper.selectByCustId(custId);
		IdentityInfo identityInfo = new IdentityInfo();
		identityInfo.setCustId(custId);
		identityInfo = identityInfoMapper.selectOne(identityInfo);
		//证件  号码
		String identityNo = "";
		//证件类型
		String identityType = "";
		if(identityInfo != null) {
			
			identityNo = identityInfo.getIdentityNo();
			//证件类型
			identityType = identityInfo.getIdentityType();
		}
		
		// 银行卡 信息
		
		// 卡号
		String cardNo = ddbIntegralWithdrawRecord.getCardNo();
//		CustomerBank customerBank = new CustomerBank();
//		customerBank.setCustId(custId);
//		customerBank.setFlag(true);
//		customerBank = customerBankMapper.selectOne(customerBank);
		// 银行名称
		String bankName = ddbIntegralWithdrawRecord.getBankName();
		// 开户行
		String activeAddress =ddbIntegralWithdrawRecord.getActiveAddress();
//		if(customerBank != null) {
//			bankName = customerBank.getBankName();
//			// 开户行
//			activeAddress = customerBank.getActiveAddress();
//			
//		}
		
		DdbIntegralWallet selectByRealNameAdnCustId = ddbIntegralWalletMapper.selectByRealNameAdnCustId("xianbei_all", custId);
		Long xianBayAmount = 0L;
		if(selectByRealNameAdnCustId != null) {
			
			xianBayAmount = selectByRealNameAdnCustId.getAmount();
		}
		
		String remark = ddbIntegralWithdrawRecord.getRemark();
		
		String account = ddbIntegralWithdrawRecord.getAccount();
		
		// 组装vo类
		IntegralwithdrawCustomerApplyVo vo = new IntegralwithdrawCustomerApplyVo();
		vo.setCardholder(cardholder==null?"":cardholder);
		vo.setAmount(amount==null?0:amount);
		vo.setApplyTime(applyTime==null?"":applyTime);
		vo.setIdentity_no(identityNo==null?"":identityNo);
		vo.setIdentity_type(identityType==null?"":identityType);
		vo.setBankName(bankName==null?"":bankName);
		vo.setActiveAddress(activeAddress==null?"":activeAddress);
		vo.setCardNo(cardNo==null?"":cardNo);
		vo.setXianBayAccount(xianBayAmount.toString());
		vo.setRemark(remark==null?"":remark);
		vo.setAccount(account==null?"":account);
		vo.setAuditTime(auditTime==null?"":auditTime);
		return vo;
	}

}
