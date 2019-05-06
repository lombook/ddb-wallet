package com.jinglitong.wallet.checkserver.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.DdbRewardProcessSeq;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholderUpgradeSeq;
import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.checkserver.common.ConstantDict;
import com.jinglitong.wallet.checkserver.mapper.CustomerMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbOrderMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbRewardProcessSeqMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbShoreholderUpgradeSeqMapper;
import com.jinglitong.wallet.checkserver.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

//@Service
@Slf4j
public class ValidataService {

	@Autowired
	private DdbRewardProcessSeqMapper ddbRewardProcessSeqMapper;
	 
	@Autowired
	private DdbOrderMapper ddbOrderMapper;
	
	@Autowired
	private CustomerMapper customerMapper; 
	
	@Autowired
	private DdbShoreholderUpgradeSeqMapper ddbShoreholderUpgradeSeqMapper;
	
	@Autowired
	private ShareholdersHierarchyValidationService shareholdersHierarchyValidationService;
	
	@Autowired
	private AliCloudMailService aliCloudMailService;
	
	//@Value("${statistics.address}")
	private String statisticsAddress;
	
	//@Value("${forward.time}")
	private int forwardTime;
	
	private String pattern ="yyyy-MM-dd HH:mm:ss";
	
	private Map<String, Integer> levelMap;
	
	public static Map<String, String> CONTENT_MAP = new HashMap<>();  //用于存放邮件内容的map，key为title,value为邮件内容
	
	public void validata() {
		log.info("ValidataService数据校验开始============");
		ConstantDict.VALIDATA_TREE_DATA.clear();
		CONTENT_MAP.clear();
		String currentTime =DateUtils.format(new Date(), pattern);
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	String beforeTime = sdf.format(DateUtils.addHours(new Date(), -forwardTime));
    	Weekend<DdbRewardProcessSeq>weekend = Weekend.of(DdbRewardProcessSeq.class);
    	WeekendCriteria<DdbRewardProcessSeq,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition("process_time >='"+beforeTime+"' and process_time <= '"+currentTime+"'  and state = 1 ");
    	weekend.setOrderByClause(" process_time asc ");
    	//查询出三小时之内的所有 reward_process_seq  state = 1 的数据
    	List<DdbRewardProcessSeq> list =  ddbRewardProcessSeqMapper.selectByExample(weekend);
    	log.info(currentTime+"到"+beforeTime+",DdbRewardProcessSeq list size ={}",list.size());
    	levelMap = getMap();
    	for (DdbRewardProcessSeq ddbRewardProcessSeq : list) {
			DdbOrder order = new DdbOrder();
			order.setFlowId(ddbRewardProcessSeq.getOrderZid());
			//根据orderid查询order
			order = ddbOrderMapper.selectOne(order);
			//获取当前用户的所有父custid
			List<String> paretsId = getAllParents(order.getUserId());
			for (int i = 0; i < paretsId.size(); i++) {
				String custId = paretsId.get(i);
				CustomerInfo4LevelDiff info =ConstantDict.VALIDATA_TREE_DATA.get(paretsId.get(i));
				if(null != info) {
					//内存中有当前用户的数据，只需更新星级级别
					int level = getLevel(ddbRewardProcessSeq.getOrderZid(), custId, DateUtils.formatDateTime(ddbRewardProcessSeq.getProcessTime()),false);
					if(level != -1) {
						info.setSelfStockholderLevel(level);
						ConstantDict.VALIDATA_TREE_DATA.put(custId, info);
					}
				}else { 
					//内存中没有当前用户的数据，需要put一个新数据
					CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
					customerInfo.setParentsList(paretsId.subList(i+1, paretsId.size()));
					int level = getLevel(ddbRewardProcessSeq.getOrderZid(), custId,  DateUtils.formatDateTime(ddbRewardProcessSeq.getProcessTime()),true);
					customerInfo.setSelfStockholderLevel(level);//查询
					ConstantDict.VALIDATA_TREE_DATA.put(custId, customerInfo);
				}
			}
			log.info("截止订单"+ddbRewardProcessSeq.getOrderZid()+"之前所有map结果集:"+JSON.toJSON(ConstantDict.VALIDATA_TREE_DATA));
			shareholdersHierarchyValidationService.startCheck(order);
		}
    	//校验完成后如有错误发送邮件
    	if(CONTENT_MAP.size() > 0){
    		StringBuffer content = new StringBuffer();
    		for (Map.Entry<String, String> emailMap : CONTENT_MAP.entrySet()) {
    			content.append(emailMap.getValue().toString());
    		}
    		String arr[] = statisticsAddress.split(",");
    		for (int n = 0; n < arr.length; n++) {
    			log.info(arr[n]+"邮件发送成功");
    			aliCloudMailService.sendMail(arr[n],  "分红拆单金额校验",content.toString());
    		}
    	}
    	log.info("ValidataService数据校验结束============");
	}
	/**
	 * 
	 * 功能说明:获取用户的由于这个订单而产生的星级别级或者三小时前最新更新的星级级别
	 * @param orderId
	 * @param custId
	 * @param beforeTime
	 * @param first 是否为第一次入内存map
	 * @return
	 */
	private int getLevel(String orderId,String custId,String currentTime,boolean first) {
		DdbShoreholderUpgradeSeq upgrade = new DdbShoreholderUpgradeSeq();
		upgrade.setCustId(custId);
		upgrade.setOrderId(orderId);
		DdbShoreholderUpgradeSeq result = ddbShoreholderUpgradeSeqMapper.selectOne(upgrade);
		if(null != result) {
			return levelMap.get(result.getNewLevelDefine());
		} /*else if(null == result && !first) {
			return -1;
		} */else {
			Weekend<DdbShoreholderUpgradeSeq>weekend = Weekend.of(DdbShoreholderUpgradeSeq.class);
	    	WeekendCriteria<DdbShoreholderUpgradeSeq,Object> Criteria =weekend.weekendCriteria();
	    	Criteria.andCondition(" cust_id ='"+custId+"' and upgrade_time < '"+currentTime+"' ");
	    	weekend.setOrderByClause("upgrade_time desc");
	    	List<DdbShoreholderUpgradeSeq> list =  ddbShoreholderUpgradeSeqMapper.selectByExample(weekend);
			if(list.size() > 0) {
				return levelMap.get(list.get(0).getNewLevelDefine());
			}
		}
		return 0;
	}
	/**
	 * 
	 * 功能说明:初始化星级对应的int
	 * @return
	 */
	private Map<String, Integer> getMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put("putong",1);
		map.put("onestar",2);
		map.put("twostar",3);
		map.put("threestar",4);
		map.put("fourstar",5);
		map.put("fivestar",6);
		return map;
	}
	/**
	 * 
	 * 功能说明:获取当前用户的所有父Id
	 * @param userId
	 * @return
	 */
	private List<String> getAllParents(String userId){
		List<String> paretsId = new ArrayList<>();
		paretsId.add(userId);
		boolean flag = true;
		do {
			String  cus = customerMapper.getinviterByinviteeCustId(userId);
			if(!StringUtils.isEmpty(cus)) {
				paretsId.add(cus);
				userId = cus;
			}else {
				flag =false;
			}
		}while(flag);
		
		return paretsId;
	}
	/**
	 * 
	 * 功能说明:对list排序，按照tree_level由低到高
	 * @param paretsId
	 * @return
	 */
/*	public List<String> sortList(List<String> paretsId) {
		List<String> currentList = new ArrayList<>();
		if(paretsId.size() == 0) {
			return currentList;
		}
		String ids ="";
		for (int i = 0; i < paretsId.size(); i++) {
			ids =ids + "'"+paretsId.get(i)+"'";
			if(i < paretsId.size() -1) {
				ids = ids + ",";
			}
		}
		Weekend<Customer>weekend = Weekend.of(Customer.class);
    	WeekendCriteria<Customer,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition("cust_id in ("+ids+")");
    	weekend.setOrderByClause("tree_level asc");
    	List <Customer> list = customerMapper.selectByExample(weekend);
    	for (int i = 0; i < list.size(); i++) {
    		currentList.add(i, list.get(i).getCustId());
		}
    	
    	return currentList;
	}*/
}
