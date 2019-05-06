package com.jinglitong.wallet.server.controller.job;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinglitong.wallet.api.feign.UnlockCoinFeignApi;
import com.jinglitong.wallet.api.model.LockCoinRecord;
import com.jinglitong.wallet.api.model.UnlockCoinRecord;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.UnLockVo;
import com.jinglitong.wallet.server.service.LockCoinRecordService;
import com.jinglitong.wallet.server.service.SwtChainService;
import com.jinglitong.wallet.server.service.UnlockCoinRecordService;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
@RequestMapping("unlockCoinJob")
@RestController
@Slf4j
public class UnlockCoinJobController implements UnlockCoinFeignApi {

	@Resource
	private LockCoinRecordService lockCoinRecordService;
	    
	@Resource
	private SwtChainService swtChainService;
	    
	@Resource
	private UnlockCoinRecordService unlockCoinRecordService;
	
	public static final String UnLOCK_COIN_RECORD_JOB_IDENTIFICATION = "unlock_coin_record_job_iden";
	/**
	 * 
	 * 功能说明:解倉轉賬
	 */
	@PostMapping("excueteTaskFeign")
	@Override
	public void excueteTask() {
		log.info("解仓开始");
		List<UnLockVo> record = lockCoinRecordService.selectByDate();
		log.info("已到期用户数为:{}",record.size());
		LockCoinRecord lo = new LockCoinRecord();
		for (UnLockVo vo : record) {
			Timestamp time = new Timestamp(new Date().getTime());
			vo.setMemo("解仓转账");
			vo.setPpwd("123456");
			vo.setUuid(UuidUtil.getUUID());
			swtChainService.payment(vo, vo.getChainId());
			UnlockCoinRecord uo =getBean(vo);//记录表
			
			//更新状态
			lo.setLockRecordId(vo.getLockRecordId());
			lo.setUpdateTime(DateUtils.getDateTime());
			String hash= "";
			if(vo.getResCode().equals(0)){//正在解
				hash =vo.getPaymentHash();
				lo.setLockStatus(3);
				uo.setStatus(2);
				uo.setTxHash(hash);
				log.info("转账成功");
			}else{//解仓失败
				lo.setLockStatus(5);
				uo.setStatus(3);
				log.info("转账失败");
			}
			uo.setUpdateTime(time);
			unlockCoinRecordService.updateRecord(lo, uo);
		}
		log.info("解仓结束");

	}
	/**
	 * 
	 * 功能说明:转账后查询状态
	 */
	@PostMapping("updateStatusFeign")
	@Override
	public void updateStatus(){
		log.info("解仓状态查询开始");
		List<UnlockCoinRecord> unrecord = unlockCoinRecordService.selectByStatus(2);
		log.info("需要查询解仓状态{}个",unrecord.size());
		LPayResVO resVO = new LPayResVO();
		LockCoinRecord lo = new LockCoinRecord();
		Timestamp time = new Timestamp(new Date().getTime());
		for (UnlockCoinRecord result : unrecord) {
			resVO.setChainId(result.getChainId());
			resVO.setTxHash(result.getTxHash());
			swtChainService.getTx(resVO);
			Map<String,Object>  map  =resVO.getResMap();
			if (resVO.getResCode().equals(0) && (Boolean) map.get("success")) {// 转账成功
				// 更新解仓记录表
				result.setStatus(1);
				result.setUpdateTime(time);
				// 更新锁仓记录表
				lo.setUpdateTime(DateUtils.getDateTime());
				lo.setLockRecordId(result.getLockRecordId());
				lo.setLockStatus(4);
			}else{//转账失败
				result.setStatus(3);
				result.setUpdateTime(time);
				// 更新锁仓记录表
				lo.setUpdateTime(DateUtils.getDateTime());
				lo.setLockRecordId(result.getLockRecordId());
				lo.setLockStatus(5);
			}
			result.setOperateCount(null);
			unlockCoinRecordService.updateStatus(result, lo);
		}
		log.info("解仓状态查询结束");
		
	}
	/**
	 * 
	 * 功能说明:组装实体UnlockCoinRecord
	 * @param vo
	 * @return
	 */
	public UnlockCoinRecord getBean(UnLockVo vo){
		Timestamp time = new Timestamp(new Date().getTime());
		UnlockCoinRecord uo = new UnlockCoinRecord();
		uo.setUnlockRecordId(UuidUtil.getUUID());
		uo.setLockRecordId(vo.getLockRecordId());
		uo.setWalletId(vo.getWalletId());
		uo.setChainId(vo.getChainId());
		uo.setCoinId(vo.getCoinId());
		uo.setAmount(vo.getAmount());
		uo.setDestAddress(vo.getDestAddress());
		uo.setFromAddress(vo.getFromAddress());
		uo.setCreateTime(time);
		uo.setUpdateTime(time);
		return uo;
	}

}
