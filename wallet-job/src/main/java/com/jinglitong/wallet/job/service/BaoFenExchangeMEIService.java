package com.jinglitong.wallet.job.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRecord;
import com.jinglitong.wallet.job.common.ErrorEnum;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.CustomerMapper;
import com.jinglitong.wallet.job.mapper.DdbAssetAccountMapper;
import com.jinglitong.wallet.job.mapper.DdbChainExchangeRecordMapper;
import com.jinglitong.wallet.job.mapper.MainChainMapper;
import com.jinglitong.wallet.job.util.UuidUtil;

@Service
public class BaoFenExchangeMEIService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 宝分兑换MEI失败次数  
	 */
	@Value("${BaoFenExchangeMEI.failCount}")
	private int count;
	/**
	 * 
	 */
	@Value("${BaoFenExchangeMEI.adminAddress}")
	private String adminEmailAddress;
	
	@Autowired
	private DdbChainExchangeRecordMapper ddbChainExchangeRecordMapper;
	@Autowired
	private DdbAssetAccountMapper ddbAssetAccountMapper;
	@Autowired
	private MainChainMapper mainChainMapper;
	
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private AliCloudMailService aliCloudMailService;
	private static final String password = "123456";

	public void start() {
		// TODO Auto-generated method stub
		// 从chain_exchange_record兑换上链表 拿数据 ,拿失败次数小于count的      状态 在0  -1、  的，进行转账        state  0未开始 1待check 2完成 -1失败     
		List<DdbChainExchangeRecord> list = ddbChainExchangeRecordMapper.getTransferDataByStateAndCount(count);
		// 转账
		exchange(list);
		// 查询失败次数 超过 count的
		List<DdbChainExchangeRecord> transferFaillist = ddbChainExchangeRecordMapper.getFailTransferDataByStateAndCount(count);
		// 检查失败几次 之后  发邮件
		sendEmailToAdmin(transferFaillist);
		
		
	}
	private void sendEmailToAdmin(List<DdbChainExchangeRecord> transferFaillist) {
		// TODO Auto-generated method stub
		if (transferFaillist != null && transferFaillist.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for(DdbChainExchangeRecord ddbChainExchangeRecord : transferFaillist) {
				String custId = ddbChainExchangeRecord.getCustId();
				Customer cus = customerMapper.selectByCustId(custId);
				String account = cus.getAccount();
				String orderId = ddbChainExchangeRecord.getOrderId();
				
				sb.append("用户").append(account).append("：宝分兑换mei的订单号为").append(orderId).append("的订单;").append("<br>");
				
				// 发完邮件  失败次数count 也加1   保证 失败的上链转账记录  只发一次邮件
				ddbChainExchangeRecordMapper.updateCountByZid(ddbChainExchangeRecord.getZid());
				
			}
			sb.append("转账失败次数超过"+count+"次,请处理");
			
			String arr[] = adminEmailAddress.split(",");
			
			for (int i = 0; i < arr.length; i++) {
				// 向管理员 发邮件
				try {
					aliCloudMailService.sendMail(arr[i], "用户宝分兑换MEI失败通知",sb.toString() );
				} catch (Exception e) {
					logger.info("用户宝分兑换MEI失败通知:" + ErrorEnum.ERROR_41006.getMsg());
				}
			}
			
			
		}
		
	}
	
	@Transactional
	private void exchange(List<DdbChainExchangeRecord> list) {
		// TODO Auto-generated method stub
		if(list != null && list.size() > 0) {
			// 遍历所有数据 进行转账处理
			for(DdbChainExchangeRecord ddbChainExchangeRecord : list) {
				
				String chainId = ddbChainExchangeRecord.getChainId();
				MainChain mainChain = mainChainMapper.selectByChainId(chainId);

				ChainService service = null;
				if (null == mainChain || StringUtils.isEmpty(mainChain.getHandleName())) {
					service = null;
					continue;
				} else {
					service = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
				}
				// 具体转账
				transferChain(ddbChainExchangeRecord,service);
			}	
		}
		
	}
	@Transactional
	private void transferChain(DdbChainExchangeRecord ddbChainExchangeRecord, ChainService service) {
		// TODO Auto-generated method stub
		String chainId = ddbChainExchangeRecord.getChainId();
		// 组装 转账类
		LPaymentVO paymentVO = new LPaymentVO();
		BigDecimal coinAmount = ddbChainExchangeRecord.getCoinAmount()==null?new BigDecimal(0):ddbChainExchangeRecord.getCoinAmount();
		
		paymentVO.setAmount(coinAmount.toString());
		String currency = ddbChainExchangeRecord.getCurrency()==null?"":ddbChainExchangeRecord.getCurrency();
		paymentVO.setCurrency(currency.toUpperCase());
		String custPublicKey = ddbChainExchangeRecord.getCustPublicKey();
		paymentVO.setDestAddress(custPublicKey);
		// 总账id
		String assetAccountId = ddbChainExchangeRecord.getAssetAccountId();
		DdbAssetAccount selectByZid = ddbAssetAccountMapper.selectByZid(assetAccountId);
		String address = selectByZid.getAddress();
		paymentVO.setFromAddress(address);
		paymentVO.setMemo("宝分上链转账");
		paymentVO.setPpwd(password);
		paymentVO.setCustid(selectByZid.getSalt());
		paymentVO.setSecretkey(selectByZid.getSecret());
		paymentVO.setUuid(UuidUtil.getUUID());
		String tokenAdress = ddbChainExchangeRecord.getTokenAdress();
		
 		// 如果非空 就赋值  银冠
		if(!StringUtil.isEmpty(tokenAdress)) {
			paymentVO.setIssuer(tokenAdress);
		}
		service.payment(paymentVO, chainId);
		String paymentHash = paymentVO.getPaymentHash();
		Integer resCode = paymentVO.getResCode();
		// 转账成功
		if (resCode == 0) {
			//修改 状态 ， hash  更新时间     状态  // 0未开始 1待check 2完成 -1失败
			ddbChainExchangeRecordMapper.updateStateHashByZid(1,paymentHash,ddbChainExchangeRecord.getZid());
			
		}else {// 转账失败
			// 修改  状态为-1 失败次数加1 更新时间
			ddbChainExchangeRecordMapper.updateStateCountByZid(-1,ddbChainExchangeRecord.getZid());
		}
	}
}
