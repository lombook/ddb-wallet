package com.jinglitong.wallet.job.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.jinglitong.wallet.api.model.FrozenReleaseAffirm;
import com.jinglitong.wallet.api.model.FrozenReleaseDetailRule;
import com.jinglitong.wallet.api.model.FrozenReleaseExcelSource;
import com.jinglitong.wallet.api.model.FrozenReleaseLogFail;
import com.jinglitong.wallet.api.model.FrozenReleaseLogs;
import com.jinglitong.wallet.api.model.FrozenReleaseRule;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.job.common.ErrorEnum;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseAffirmMapper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseDetailRuleMapper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseExcelSourceMapper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseLogFailMapper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseLogsMapper;
import com.jinglitong.wallet.job.mapper.FrozenReleaseRuleMapper;
import com.jinglitong.wallet.job.mapper.MainChainMapper;
import com.jinglitong.wallet.job.mapper.SubChainMapper;
import com.jinglitong.wallet.job.util.DateUtils;
import com.jinglitong.wallet.job.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CheckReleaseForEmailService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 管理员邮箱地址
	 */
	@Value("${RealaseAdminEmailAddress}")
	private String realaseAdminEmailAddress;
	
	/**
	 * 指定删除几天前的
	 */
	@Value("${log_fail.ReleaseSuccessDeleteFewDaysAgo.days}")
	private int needDeleteDay;

	@Autowired
	private FrozenReleaseLogFailMapper frozenReleaseLogFailMapper;
	@Autowired
	private FrozenReleaseDetailRuleMapper frozenReleaseDetailRuleMapper;
	@Autowired
	private FrozenReleaseExcelSourceMapper frozenReleaseExcelSourceMapper;
	@Autowired
	private AliCloudMailService aliCloudMailService;

	@Autowired
	private MainChainMapper mainChainMapper;
	@Autowired
	private FrozenReleaseRuleMapper frozenReleaseRuleMapper;
	@Autowired
	private SubChainMapper subChainMapper;
	@Autowired
	private FrozenReleaseAffirmMapper frozenReleaseAffirmMapper;
	@Autowired
	private FrozenReleaseLogsMapper frozenReleaseLogsMapper;

	private static final String password = "123456";
	
	public void start() {
		// 查询转账失败次数小于3的 再次进行转账
		List<FrozenReleaseLogFail> selectByCountLessThree = frozenReleaseLogFailMapper.selectByCountLessThree();
		// 转账
		exchange(selectByCountLessThree);
		// 查询转账失败次数 大于3,小于等于4的    并且发送失败的    发送邮件给管理员
		List<FrozenReleaseLogFail> selectByCountMoreThree = frozenReleaseLogFailMapper.selectByCountMoreThree();
		//  发送邮件给管理员
		sendEmailToAdmin(selectByCountMoreThree);
		
		// 删除 指定几天前  释放成功的  日志
		deleteFewDaysAgoLogs();
		
	}
	
	private void deleteFewDaysAgoLogs() {
		// TODO Auto-generated method stub
		String delayTime = DateUtils.getDelayTime(-needDeleteDay,2,new Date());
		frozenReleaseLogFailMapper.deleteBySuccessTime(delayTime);
	}

	private void sendEmailToAdmin(List<FrozenReleaseLogFail> selectByCountMoreThree) {
		// TODO Auto-generated method stub
		if (selectByCountMoreThree != null && selectByCountMoreThree.size() > 0) {
			StringBuffer sb = new StringBuffer();

			for (FrozenReleaseLogFail frozenReleaseLogFail : selectByCountMoreThree) {

				String detailId = frozenReleaseLogFail.getDetailId();
				// 用户钱包
//				String targetAddress = frozenReleaseLogFail.getTargetAddress();

				FrozenReleaseLogs selectByLogId = frozenReleaseLogsMapper.selectByLogId(frozenReleaseLogFail.getLogId());
				String walletId = selectByLogId.getWalletId();
				// 根据detailId查找 获得 rule_id
				FrozenReleaseDetailRule selecByDetailId = frozenReleaseDetailRuleMapper.selecByDetailId(detailId);

				String ruleId = "";
				if (selecByDetailId != null) {

					ruleId = selecByDetailId.getRuleId();
				}

				// 根据rule_id 和 钱包地址 查找 用户
				FrozenReleaseExcelSource selectSourceByWalletIdRuleId = frozenReleaseExcelSourceMapper
						.selectSourceByWalletIdRuleId(walletId, ruleId);
				String account = "";
				if (selectSourceByWalletIdRuleId != null) {
					account = selectSourceByWalletIdRuleId.getAccount();
					// 过滤重复账号
					if(sb.indexOf(account) < 0) {
						
						sb.append(account).append(",");
					}
				}
				// 每次count也要自加一
				// 每次发完邮件  也要   修改 logs_fail表的次数   防止再次重复发送
				frozenReleaseLogFailMapper.updateCountByZid(frozenReleaseLogFail.getZid());
				
			}
			if(sb != null && sb.length() > 0 ) {
				
				String emailstring = sb.toString();
				emailstring= emailstring.substring(0, emailstring.length()-1);
				String[] emailSplit = emailstring.split(",");
				sb  = new StringBuffer();
				// 遍历emailSplit  每5个账号   就换行
				for(int i = 0;i<emailSplit.length;i++) {
					
					if(i%4 == 0) {
						sb.append(emailSplit[i]).append(",").append("\r");
					}else {
						sb.append(emailSplit[i]).append(",");
					}
					
				}
				emailstring = sb.toString();
				emailstring= emailstring.substring(0, emailstring.length()-1);
				String arr[] = realaseAdminEmailAddress.split(",");
				for (int i = 0; i < arr.length; i++) {
					// 向管理员 发邮件
					try {
						aliCloudMailService.sendMail(arr[i], "检查释放转账失败通知", "以上账户:" + emailstring + "转账失败，请处理");
					} catch (Exception e) {
						log.info("检查释放转账失败通知:" + ErrorEnum.ERROR_41006.getMsg());
					}
				}
				
			}
		}
	}
	@Transactional
	private void exchange(List<FrozenReleaseLogFail> selectByCountLessThree) {
		if (selectByCountLessThree != null && selectByCountLessThree.size() > 0) {

			ChainService service = null;

			for (FrozenReleaseLogFail frozenReleaseLogFail : selectByCountLessThree) {
				String detailId = frozenReleaseLogFail.getDetailId();
				// 用户钱包
//				String targetAddress = frozenReleaseLogFail.getTargetAddress();
				
				// 根据detailId查找 获得 rule_id
				FrozenReleaseDetailRule selecByDetailId = frozenReleaseDetailRuleMapper.selecByDetailId(detailId);
				String ruleId = "";
				if (selecByDetailId != null) {

					ruleId = selecByDetailId.getRuleId();
				}

				FrozenReleaseLogs selectByLogId = frozenReleaseLogsMapper.selectByLogId(frozenReleaseLogFail.getLogId());
//				String walletId = selectByLogId.getWalletId();
				// 根据rule_id 和 钱包地址 查找 用户
//				FrozenReleaseExcelSource selectSourceByWalletIdRuleId = frozenReleaseExcelSourceMapper
//						.selectSourceByWalletIdRuleId(selectByLogId.getWalletId(), ruleId);
				String walletId = selectByLogId.getWalletId();
//				if (selectSourceByWalletIdRuleId != null) {
//					walletId = selectSourceByWalletIdRuleId.getWalletId();
//				}
				FrozenReleaseRule selectByRuleId = frozenReleaseRuleMapper.selectByRuleId(ruleId);
				
				MainChain mainChain =null;
				
				if(selectByRuleId != null) {
					mainChain = mainChainMapper.selectByChainId(selectByRuleId.getChianId());
				}

//				String chainCurrency = "";
				if (null == mainChain || StringUtils.isEmpty(mainChain.getHandleName())) {
					service = null;
					continue;
				} else {
					service = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
//					chainCurrency = mainChain.getChainCurrency();
				}
				// 具体转账方法
				transferChain(frozenReleaseLogFail,detailId,ruleId,walletId,selectByRuleId,service);
			}

		}

	}
	@Transactional
	public void transferChain(FrozenReleaseLogFail frozenReleaseLogFail,String detailId,String ruleId,String walletId,FrozenReleaseRule selectByRuleId,ChainService service){
		
		String coinId = "";
		String payPasswd = "";
		String chianId = "";
		if (selectByRuleId != null) {
			coinId = selectByRuleId.getCoinId();
			payPasswd = selectByRuleId.getPayPasswd();
			chianId = selectByRuleId.getChianId();
		}
		SubChain selectByCoinId = subChainMapper.selectByCoinId(coinId);
		Boolean baseChain = null;
		String tokenAddress = "";
		String currency = "";
		if (selectByCoinId != null) {
			// 是否主币1是0不是
			baseChain = selectByCoinId.getBaseChain();
			// 银冠
			tokenAddress = selectByCoinId.getTokenAddress();
			if(tokenAddress == null) {
				tokenAddress = "";
			}
			currency = selectByCoinId.getCurrency();
		}

		if (service != null) {
			
			LPaymentVO paymentVO = new LPaymentVO();
			paymentVO.setAmount(frozenReleaseLogFail.getSendAmount());
			paymentVO.setCurrency(currency.toUpperCase());
			paymentVO.setDestAddress(frozenReleaseLogFail.getTargetAddress());
			paymentVO.setFromAddress(frozenReleaseLogFail.getLedgerAdress());
			paymentVO.setMemo("冻结资产释放");
			paymentVO.setPpwd(password);
			paymentVO.setCustid(ruleId);
			paymentVO.setSecretkey(payPasswd);
			paymentVO.setUuid(UuidUtil.getUUID());

			// 设置银冠 不是主币 设置银冠
//			if (baseChain != null && !baseChain) {
//				paymentVO.setIssuer(tokenAddress);
//			}
			// 如果非空 就赋值
			if(!StringUtil.isEmpty(tokenAddress)) {
				paymentVO.setIssuer(tokenAddress);
			}
			service.payment(paymentVO, chianId);
			String paymentHash = paymentVO.getPaymentHash();
			Integer resCode = paymentVO.getResCode();
			FrozenReleaseAffirm frozenReleaseAffirm = null;
			String dateTime = DateUtils.getDateTime();
			// 转账成功
			if (resCode == 0) {
				logger.debug("释放转账成功" + paymentVO.toString());
				// 插入affirm表
				frozenReleaseAffirm = new FrozenReleaseAffirm();
				frozenReleaseAffirm.setCreateTime(dateTime);
				// String detailId = UuidUtil.getUUID();
				frozenReleaseAffirm.setDetailId(detailId);
				frozenReleaseAffirm.setWalletId(walletId);
				frozenReleaseAffirm.setPayHash(paymentHash);
				frozenReleaseAffirm.setLogId(frozenReleaseLogFail.getLogId());
				frozenReleaseAffirmMapper.insert(frozenReleaseAffirm);
				// 修改logs表  状态  和  给hash赋值
				frozenReleaseLogsMapper.updateLogStatusByLogId(2,paymentHash,frozenReleaseLogFail.getLogId());
				// 修改log_fail表的状态，时间
				frozenReleaseLogFailMapper.updateSuccessStateByZid(1,frozenReleaseLogFail.getZid());
			} else {
				// 转账失败 修改 logs_fail表的次数
				frozenReleaseLogFailMapper.updateCountByZid(frozenReleaseLogFail.getZid());

			}

		}

	}
	
	
}
