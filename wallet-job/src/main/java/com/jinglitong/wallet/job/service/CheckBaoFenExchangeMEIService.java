package com.jinglitong.wallet.job.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.ddbapi.model.DdbAssetAccount;
import com.jinglitong.wallet.ddbapi.model.DdbChainExchangeRecord;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.DdbAssetAccountMapper;
import com.jinglitong.wallet.job.mapper.DdbChainExchangeOrderMapper;
import com.jinglitong.wallet.job.mapper.DdbChainExchangeRecordMapper;
import com.jinglitong.wallet.job.mapper.MainChainMapper;
import com.jinglitong.wallet.job.util.DateUtils;
import com.jinglitong.wallet.job.util.UuidUtil;

@Service
public class CheckBaoFenExchangeMEIService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 查询 几秒钟之前的     
	 */
	@Value("${CheckBaoFenExchangeMEI.time}")
	private int secondtime;
	
	@Autowired
	private DdbChainExchangeRecordMapper ddbChainExchangeRecordMapper;
	@Autowired
	private DdbAssetAccountMapper ddbAssetAccountMapper;
	@Autowired
	private DdbChainExchangeOrderMapper ddbChainExchangeOrderMapper;
	@Autowired
	private MainChainMapper mainChainMapper;
	private static final String password = "123456";

	public void start() {
		// TODO Auto-generated method stub
		// 从chain_exchange_record兑换上链表 拿数据 ,拿state=1的 ，更新时间是当前时间minute之前的    进行确认
		// 0未开始 1待check 2完成 -1失败
		String delayTime = DateUtils.getDelayTime(-secondtime, 1,new Date());
		List<DdbChainExchangeRecord> list = ddbChainExchangeRecordMapper.getCheckDataByState(delayTime);
		// 检查是否转账成功
		checkTransferData(list);
		
	}
	
	private void checkTransferData(List<DdbChainExchangeRecord> list) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				if(list != null && list.size() > 0) {
					// 遍历所有数据 检查是否转账成功
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
						// 检查
						checkMethod(ddbChainExchangeRecord,service);
					}	
				}
				
	}
	
	
	
	private void checkMethod(DdbChainExchangeRecord ddbChainExchangeRecord, ChainService service) {
		// TODO Auto-generated method stub
		LPayResVO resVO = new LPayResVO();
        resVO.setChainId(ddbChainExchangeRecord.getChainId());
        resVO.setTxHash(ddbChainExchangeRecord.getPayHash());
        service.getTx(resVO);
        if (resVO.getResCode().equals(0)) {
            logger.debug("验证"+ddbChainExchangeRecord.getId());
            //判断是否成功
            //成功
            Map<String, Object> resMap =resVO.getResMap();
            Object success = resMap.get("success");
            if("true".equals(success.toString())){
            	// 转账成功
            	// 修改 兑换上链表  状态  为2  (0未开始 1待check 2完成 -1失败)
            	ddbChainExchangeRecordMapper.updateStateByZid(2,ddbChainExchangeRecord.getZid());
            	
            	// 修改 chain_exchange_order 币交易订单表  状态  为1   0:未完成 1:完成  
            	ddbChainExchangeOrderMapper.updateStateByZid(1,ddbChainExchangeRecord.getOrderId());
            }else {
            	// 修改 兑换上链表  状态  为 -1 (0未开始 1待check 2完成 -1失败)
            	ddbChainExchangeRecordMapper.updateStateByZid(-1,ddbChainExchangeRecord.getZid());
            }
        }else {
        	 logger.debug("验证出错"+resVO.getMessage());
        }
	}
}
