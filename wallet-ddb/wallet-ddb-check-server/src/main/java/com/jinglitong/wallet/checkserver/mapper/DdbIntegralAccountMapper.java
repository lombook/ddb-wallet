package com.jinglitong.wallet.checkserver.mapper;

import com.jinglitong.wallet.checkserver.util.MyMapper;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccountSeq;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DdbIntegralAccountMapper extends MyMapper<DdbIntegralAccount> {
	
	/**
	 * 查询总账变更
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return 
	 */
	Map<String, Object> selectSumCustIntegralRecord(String beginTime, String endTime);
	
	/**
	 * 查询钱包变更
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return 
	 */
	Map<String, Object> selectSumIntegralAccountSeq(String beginTime, String endTime);
	
	/**
	 * 查询转账记录
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return 
	 */
	Map<String, Object> selectSumIntegralWalletSeq(String beginTime, String endTime);

	Map<String, Object> selectByRecordRealName(String beginTime, String endTime,String realName,int type);

	Map<String, Object> selectByAccountRealName(String beginTime, String endTime,String realName,int type);

	Map<String, Object> selectByWalletRealName(String beginTime, String endTime,String realName,int type);

	Map<String, Object> selectAccountRealNameBitAfter(String beginTime, String endTime,String realName,String order);
	Map<String, Object> selectAccountRealNamebefore(String beginTime, String endTime,String realName,String order);

	List<Map<String, Object>> selectRealAmountType(String beginTime, String endTime, int type);

	Map<String, Object> selectWalletRealNameBit(String beginTime, String endTime,String inteName,String order,String custId);

	Map<String, Object> selectWalletRealNameBitbefore(String beginTime, String endTime,String inteName,String order,String custId);


	Map<String, Object> selectRealWalletType(String beginTime, String endTime, int i,String inteName,String custId);

	List<Map<String,Object>> selectCustId(String beginTime, String endTime);

	List<DdbIntegralAccount> DdbIntegralAccount();

	List<DdbIntegralAccount> selectIntegeName(String beginTime, String endTime);
	
	DdbIntegralAccount selectByRealName(String realName);
	void updCustomerStatus(Map map);

    Map<String,String> selectCustomerByCustId(String cust_id);

    Map<String,Object> selectRealRecordType(String beginTime, String endTime, int i, String integName, String cust_id);


	List<DdbCustIntegralRecord> selectByRecordByIntegName(String beginTime, String endTime, String integName);

	List<DdbIntegralAccountSeq> selectAccountSeqByIntegName(String beginTime, String endTime, String integName);

	List<Map<String ,String>> selectDdbIntegralGroupName(@Param("beginTime") String beginTime,@Param("endTime") String endTime);
	List<Map<String,String>> selectDdbIntegralNames();
	/**
	 * 按照integerName 查询总账 总账记录 钱包帐
	 * @param integName 大类型
	 * @return
	 */
	List<Long> selectDdbIntegralByIntegName(@Param("integName") String integName);
	 List<Long> selectDdbIntegralByRealName(@Param("integName") String integName);
}
