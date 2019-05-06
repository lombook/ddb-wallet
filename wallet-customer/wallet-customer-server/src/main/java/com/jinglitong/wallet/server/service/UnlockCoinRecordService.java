/*
 * Copyright 2018 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */
package com.jinglitong.wallet.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.server.mapper.LockCoinRecordMapper;
import com.jinglitong.wallet.server.mapper.UnlockCoinRecordMapper;
import com.jinglitong.wallet.api.model.LockCoinRecord;
import com.jinglitong.wallet.api.model.UnlockCoinRecord;

/**
 * Copyright (c) 2018, 北京井立通科技有限公司
 * All rights reserved.
 * 文件名称: UnlockCoinRecordService.java
 * 作        者: yxl 2018年6月27日
 * 创建时间: 2018年6月27日
 * 功能说明: 解仓service
 */

@Service
public class UnlockCoinRecordService {

	@Autowired
	private UnlockCoinRecordMapper unlockCoinRecordMapper;
	
	@Autowired
    private LockCoinRecordMapper lockCoinRecordMapper;
	/**
	 * 
	 * 功能说明:解仓记录表
	 * @param record
	 * @return
	 */
	public int insertAll(UnlockCoinRecord record){
		return unlockCoinRecordMapper.insertAll(record);
	}
	/**
	 * 
	 * 功能说明:根据id修改记录表
	 * @param record
	 * @return
	 */
	public int updateById(UnlockCoinRecord record){
		return unlockCoinRecordMapper.updateById(record);
	}
	
	public UnlockCoinRecord selectOne(String lockRecordId){
		UnlockCoinRecord record = new UnlockCoinRecord();
		record.setLockRecordId(lockRecordId);
		return unlockCoinRecordMapper.selectOne(record);
	}
	/**
	 * 
	 * 功能说明:根据状态查询解仓详情
	 * @param status
	 * @return
	 */
	public List<UnlockCoinRecord> selectByStatus(int status){
		
		return unlockCoinRecordMapper.selectByStatus(status);
	}
 	
	/**
	 * 
	 * 功能说明:
	 * @param lo 更新锁仓表
	 * @param res 更新解仓表
	 * @param uo 插入解仓表
	 * @param vo 需要解仓的数据
	 * @param hash 转账hash
	 */
	@Transactional
	public void updateRecord(LockCoinRecord lo,UnlockCoinRecord uo) {
		lockCoinRecordMapper.updateStatusById(lo);
		unlockCoinRecordMapper.insertAll(uo);
	}
    
    /**
     * 
     * 功能说明:
     * @param result 解仓表修改状态
     * @param lo 锁仓表修改状态
     */
	@Transactional
    public void updateStatus(UnlockCoinRecord result,LockCoinRecord lo) {
    	unlockCoinRecordMapper.updateById(result);
    	lockCoinRecordMapper.updateStatusById(lo);
    }
}
