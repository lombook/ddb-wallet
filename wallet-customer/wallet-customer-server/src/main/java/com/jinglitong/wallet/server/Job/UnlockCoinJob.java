///*
// * Copyright 2018 Zhongan.com All right reserved. This software is the
// * confidential and proprietary information of Zhongan.com ("Confidential
// * Information"). You shall not disclose such Confidential Information and shall
// * use it only in accordance with the terms of the license agreement you entered
// * into with Zhongan.com.
// */
//package com.jinglitong.wallet.server.Job;
//
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.jinglitong.wallet.api.model.LockCoinRecord;
//import com.jinglitong.wallet.api.model.UnlockCoinRecord;
//import com.jinglitong.wallet.api.model.logic.LPayResVO;
//import com.jinglitong.wallet.api.model.logic.UnLockVo;
//import com.jinglitong.wallet.server.service.LockCoinRecordService;
//import com.jinglitong.wallet.server.service.SwtChainService;
//import com.jinglitong.wallet.server.service.UnlockCoinRecordService;
//import com.jinglitong.wallet.server.util.DateUtils;
//import com.jinglitong.wallet.server.util.UuidUtil;
//
//
///**
// * Copyright (c) 2018, 北京井立通科技有限公司
// * All rights reserved.
// * 文件名称: UnlockCoinJob.java
// * 作        者: yxl 2018年6月27日
// * 创建时间: 2018年6月27日
// * 功能说明: 解仓定时任务
// */
//@Component
//public class UnlockCoinJob {
//
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Resource
//    private LockCoinRecordService lockCoinRecordService;
//
//    @Resource
//    private SwtChainService swtChainService;
//
//    @Resource
//    private UnlockCoinRecordService unlockCoinRecordService;
//
//    public static final String UnLOCK_COIN_RECORD_JOB_IDENTIFICATION ="unlock_coin_record_job_iden";
//
//    /**
//     * 任务开关
//     */
//    private int unlockCoinJobOpen = 1;
//
//	public void UnclockCoin(){
//		if(unlockCoinJobOpen!=1){
//			logger.info("解仓任务开关:关闭");
//            return;
//        }
//		 try {
//			 if(null != redisTemplate.opsForValue().get(UnLOCK_COIN_RECORD_JOB_IDENTIFICATION) && (int)redisTemplate.opsForValue().get(UnLOCK_COIN_RECORD_JOB_IDENTIFICATION)==1){
//	                logger.error("任务再跑着，下次吧！");
//	            }else {
//	                redisTemplate.opsForValue().set(UnLOCK_COIN_RECORD_JOB_IDENTIFICATION,1);
//	                excueteTask();
//	            }
//			Thread.sleep(30000);
//		} catch (Exception e) {
//			logger.error("错误："+e.getMessage());
//            e.printStackTrace();
//		}finally {
//			updateStatus();
//            //删除标识
//            redisTemplate.delete(UnLOCK_COIN_RECORD_JOB_IDENTIFICATION);
//        }
//
//
//
//	}
//
//	public void excueteTask(){
//		logger.info("解仓开始");
//		List<UnLockVo> record = lockCoinRecordService.selectByDate();
//		logger.info("已到期用户数为:{}",record.size());
//		LockCoinRecord lo = new LockCoinRecord();
//		for (UnLockVo vo : record) {
//			Timestamp time = new Timestamp(new Date().getTime());
//			vo.setMemo("解仓转账");
//			vo.setPpwd("123456");
//			vo.setUuid(UuidUtil.getUUID());
//			swtChainService.payment(vo, vo.getChainId());
//			UnlockCoinRecord uo =getBean(vo);//记录表
//
//			//更新状态
//			lo.setLockRecordId(vo.getLockRecordId());
//			lo.setUpdateTime(DateUtils.getDateTime());
//			String hash= "";
//			if(vo.getResCode().equals(0)){//正在解
//				hash =vo.getPaymentHash();
//				lo.setLockStatus(3);
//				uo.setStatus(2);
//				uo.setTxHash(hash);
//				logger.info("转账成功");
//			}else{//解仓失败
//				lo.setLockStatus(5);
//				uo.setStatus(3);
//				logger.info("转账失败");
//			}
//			uo.setUpdateTime(time);
//			unlockCoinRecordService.updateRecord(lo, uo);
//		}
//		logger.info("解仓结束");
//	}
//
//	public void updateStatus(){
//		logger.info("解仓状态查询开始");
//		List<UnlockCoinRecord> unrecord = unlockCoinRecordService.selectByStatus(2);
//		logger.info("需要查询解仓状态{}个",unrecord.size());
//		LPayResVO resVO = new LPayResVO();
//		LockCoinRecord lo = new LockCoinRecord();
//		Timestamp time = new Timestamp(new Date().getTime());
//		for (UnlockCoinRecord result : unrecord) {
//			resVO.setChainId(result.getChainId());
//			resVO.setTxHash(result.getTxHash());
//			swtChainService.getTx(resVO);
//			Map<String,Object>  map  =resVO.getResMap();
//			if (resVO.getResCode().equals(0) && (Boolean) map.get("success")) {// 转账成功
//				// 更新解仓记录表
//				result.setStatus(1);
//				result.setUpdateTime(time);
//				// 更新锁仓记录表
//				lo.setUpdateTime(DateUtils.getDateTime());
//				lo.setLockRecordId(result.getLockRecordId());
//				lo.setLockStatus(4);
//			}else{//转账失败
//				result.setStatus(3);
//				result.setUpdateTime(time);
//				// 更新锁仓记录表
//				lo.setUpdateTime(DateUtils.getDateTime());
//				lo.setLockRecordId(result.getLockRecordId());
//				lo.setLockStatus(5);
//			}
//			result.setOperateCount(null);
//			unlockCoinRecordService.updateStatus(result, lo);
//		}
//		logger.info("解仓状态查询结束");
//
//	}
//	public UnlockCoinRecord getBean(UnLockVo vo){
//		Timestamp time = new Timestamp(new Date().getTime());
//		UnlockCoinRecord uo = new UnlockCoinRecord();
//		uo.setUnlockRecordId(UuidUtil.getUUID());
//		uo.setLockRecordId(vo.getLockRecordId());
//		uo.setWalletId(vo.getWalletId());
//		uo.setChainId(vo.getChainId());
//		uo.setCoinId(vo.getCoinId());
//		uo.setAmount(vo.getAmount());
//		uo.setDestAddress(vo.getDestAddress());
//		uo.setFromAddress(vo.getFromAddress());
//		uo.setCreateTime(time);
//		uo.setUpdateTime(time);
//		return uo;
//	}
//
//}
