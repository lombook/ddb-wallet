package com.jinglitong.wallet.ddbkjserver.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.ddbapi.model.logic.SecExpandDicInfo;
import com.jinglitong.wallet.ddbkjserver.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbapi.model.logic.RewardRuleInfo;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.util.DateUtils;
import com.jinglitong.wallet.ddbkjserver.util.UuidUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * 订单分红处理：订单金额累计&股东升级&团队销售分红处理
 */
@Service
@Slf4j
@Transactional
public class HandlerOrderService {
	@Autowired
	private DdbRewardRuleMapper ddbRewardRuleMapper;
	@Autowired
	private DdbRewardGfRuleMapper ddbRewardGfRuleMapper;
	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	@Autowired
	private DdbShoreholderRuleMapper ddbShoreholderRuleMapper;
	@Autowired
	private DdbShoreholderUpgradeSeqMapper ddbShoreholderUpgradeSeqMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private DdbShoreholderMapper ddbShoreholderMapper;
	@Autowired
	private DdbRewardProcessSeqMapper ddbRewardProcessSeqMapper;
	@Autowired
	private DdbRewardGfProcessSeqMapper ddbRewardGfProcessSeqMapper;
	@Autowired
	private DdbOrderMapper ddbOrderMapper;
	@Autowired
	private DdbGrowSeedMapper ddbGrowSeedMapper;
	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	@Autowired
	private DdbRewardRuleDetailMapper ddbRewardRuleDetailMapper;
	@Autowired
	private DdbRewardGfRuleDetailMapper ddbRewardGfRuleDetailMapper;
	@Autowired
	private DdbSeplitOrderRewardMapper ddbSeplitOrderRewardMapper;
	@Autowired
	private DdbSeplitOrderRewardGfMapper ddbSeplitOrderRewardGfMapper;
	@Autowired
	private IntegerExchangeService integerExchangeService;
	@Autowired
	private TeamSumProductMQService teamSumProductMQService;
	@Autowired
	private DdbLevelupWelfareRuleMapper ddbLevelupWelfareRuleMapper;
	@Autowired
	private DdbLevelupWelfareRecordMapper ddbLevelupWelfareRecordMapper;

	public void handlerOrder(KJNotice order){
		System.out.println("begin handle order");
		System.out.println("recive order info:"+order.toString());

		//输入数据校验。
		if("1".equals(order.getType())) {
			if (StringUtils.isBlank(order.getRuleId())) {
				// 判断分红规则ID是否存在，若不存在，则该订单不参与分红，处理结束
				preFinishProcess(order);
				return;
			}
			if (StringUtils.isBlank(order.getCustId())) {
				log.error("handlerOrder:: input KJNotice error. custId is empty. input order flowId-->"+order.getFlowId());
				preFinishProcess(order);
				return;
			}
			if (StringUtils.isBlank(order.getFlowId())) {
				log.error("handlerOrder:: input KJNotice error. flowId is empty. input order-->"+order.toString());
				preFinishProcess(order);
				return;
			}
			if (StringUtils.isBlank(order.getProductNum())) {
				log.error("handlerOrder:: input KJNotice error. productNum is empty. input order flowId-->"+order.getFlowId());
				preFinishProcess(order);
				return;
			}
		} else if("2".equals(order.getType())) {
			if (StringUtils.isBlank(order.getCustId())) {
				log.error("handlerOrder:: input KJNotice error. custId is empty. input seed order zid-->"+order.getFlowId());
				preFinishProcess(order);
				return;
			}
			if (StringUtils.isBlank(order.getFlowId())) {
				log.error("handlerOrder:: input KJNotice error. flowId is empty. input seed order-->"+order.toString());
				preFinishProcess(order);
				return;
			}
			if (order.getAmount()==null) {
				log.error("handlerOrder:: input KJNotice error. amount is null. input seed order zid-->"+order.getFlowId());
				preFinishProcess(order);
				return;
			}
		}

		if("1".equals(order.getType())) {
			// 处理订单
			processOrder(order);
			if(order.getGfValue()>0)
				processOrder2(order);
		} else if("2".equals(order.getType())) {
			// 处理育苗订单
			//processSeedOrder(order);
		}
	}

	/**
	 * 处理订单
	 * @param order
	 */
	public void processOrder(KJNotice order){
		//1、根据输入的规则ID（ruleId），查询商品规则表，条件字段zid=ruleId，
		//获得产品价格(product_price)及分红规则(frule_group)。
		DdbGoodsRule goodsRuleInfo = getGoodsRuleInfoByRuleId(order.getRuleId());

		//1.1、计算获得订单总金额（单位分）
		//订单总金额(orderAmount) = 输入的商品数量（productNum）* 产品价格(product_price)
		BigDecimal orderAmount = new BigDecimal(order.getProductNum()).multiply(new BigDecimal(goodsRuleInfo.getProductPrice()));
		//订单金额保存到订单
		order.setAmount(orderAmount);
		//1.2、组装分红规则Map
		Map<String, RewardRuleInfo> rewardRuleMap = makeRewardRuleMap(goodsRuleInfo.getFruleGroup());
		if(rewardRuleMap == null || rewardRuleMap.size() < 1) {
			preFinishProcess(order);
			return;
		}
		//根据1中获取的分红规则(frule_group)，查询分红规则表及分红规则明细，条件字段frule_group，获得分红规则明细List。
		//解析分红规则表分红参数(reward_args)，根据reward_args中的real_name，查询总账表，条件字段real_name，获得业务id(zid)。
		//组装分红规则Map<rule_type, RewardRuleInfo>
		//com.jinglitong.wallet.ddbapi.model.logic.RewardRuleInfo
		//RewardRuleInfo包含：规则类型rule_type、
		//                   分红基数比例reward_rate、
		//                   积分分红参数reward_argsMap<积分名称(real_name), 总账积分ZID(inte_zid)+":"+分红比例(rate)>、
		//                   拓展分红比例reward_percent（规则类型rule_type=1or2时有值,3or4时为空）、
		//                   分红比例明细Map<股东等级名称(shoreholder_level_define), 分红规则明细ZID+":"+分红比例(reward_percent)>（规则类型rule_type=4时有值,1or2or3时为空）

		//2、获得股东升级规则
		Map<String, DdbShoreholderRule> shoreholderRuleMap = getShoreholderRuleMap();


		//3、判断该订单用户在树中是否存在。若不存在，则认为是新注册用户，添加到树中。（废弃）

		//4、个人业绩累计
		//4.1、该订单用户个人消费金额累计
		cumulateSelfSumAmountByUserid(order.getCustId(), orderAmount);

		//4.2、该订单用户团队业绩的累计
		cumulateTeamSumAmountByUserid(order.getCustId(), orderAmount);


		//发送报表需要的自己的金额
		teamSumProductMQService.sendTeamSumToMq(order.getCustId(),order,DateUtils.getDateTime());

		//4.3、该订单用户个人购买特定商品数量累计
		cumulateOwnSpecialNumByUserid(order.getCustId(), order.getSpecialNum());

		/*
		//个人股东升级，满足则更新
		int newLevel = updateShareholderLevel(order.getCustId(), orderAmount, shoreholderRuleMap);
		int oldLevel = ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId()).getSelfStockholderLevel();
		if(newLevel-oldLevel>0){
			//更新自己股东等级
			ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId()).setSelfStockholderLevel(newLevel);
			//更新直接上级团队成员中各股东人数
			updateParentsTeamMemberCnt(order.getCustId(),oldLevel,newLevel);
			String newLevelStr = shoreholderRuleMap.get(newLevel+"").getLevelDefine();
			//更新DB
			updateShoreholder(order.getCustId(),order.getType(),newLevelStr,order.getSpecialNum(),0);
			//保存股东流水表
			saveShoreholder(order.getCustId(), newLevelStr,order);
			//股东升级后，根据新级别发放福利奖励
			upgradeBenefits(order.getCustId(), newLevelStr, order.getFlowId());
		}else {
			//更新DB
			String oldLevelStr = null;
			DdbShoreholderRule ddbShoreholderRule = shoreholderRuleMap.get(oldLevel+"");
			if(ddbShoreholderRule!=null){
				oldLevelStr = ddbShoreholderRule.getLevelDefine();
			}
			updateShoreholder(order.getCustId(),order.getType(),oldLevelStr,order.getSpecialNum(),0);
		}
		*/
		
		//5、该订单用户股东升级：判断是否满足升级条件，若满足则触发升级
		checkAndUpgradeShareholderLevel(order, order.getCustId(), 0, shoreholderRuleMap);
		
		/**
		 * 6、逐级股东升级：
		 * 		循环更新上级团队金额、股东升级、逐一更新各个上级股东级别个数、直接下级购买的特殊商品数量、更新DB及股东升级流水
		 */
		updateParentsCustomerInfo(order, shoreholderRuleMap);

		//7、开始股东分红
		//7.1.根据用户id获取该用户的层级信息。分析该用户的层级信息。
		CustomerInfo4LevelDiff userHierarchyInfo = ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId());

		if(userHierarchyInfo != null){
			//7.2.一级拓展分红，插入拆单表
			startLevelExtendBonus(order,new BigDecimal(order.getRcValue()),rewardRuleMap,userHierarchyInfo);
			//7.3.二级扩展分红
			secondLevelExtendBonus(order,orderAmount,ConstantDict.INIT_SECOND_SYSTEM.get(""+order.getRuleId()),userHierarchyInfo);
			//7.4.极差分红&加权分红，插入拆单表
			stageBonus(order,new BigDecimal(order.getRcValue()),rewardRuleMap,userHierarchyInfo);
		}
		//8.结束前处理：回填DB，更新该条记录状态为已处理
		preFinishProcess(order);

		//9. 转账处理，回填DB，更新订单状态  add by liangtengfei
		integerExchangeService.exChange(order);

	}


	/**
	 * 处理订单
	 * @param order
	 */
	public void processOrder2(KJNotice order){
		//1、根据输入的规则ID（ruleId），查询商品规则表，条件字段zid=ruleId，
		//获得产品价格(product_price)及分红规则(frule_group)。
		DdbGoodsRule goodsRuleInfo = getGoodsRuleInfoByRuleId(order.getRuleId());

		//1.2、组装分红规则Map
		Map<String, RewardRuleInfo> rewardRuleMap = makeRewardRuleMap2(goodsRuleInfo.getFruleGroup());
		if(rewardRuleMap == null || rewardRuleMap.size() < 1) {
			preFinishProcess2(order);
			return;
		}


		//7、开始股东分红
		//7.1.根据用户id获取该用户的层级信息。分析该用户的层级信息。
		CustomerInfo4LevelDiff userHierarchyInfo = ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId());

		if(userHierarchyInfo != null){
			//7.2.一级拓展分红，插入拆单表
			startLevelExtendBonus2(order,new BigDecimal(order.getGfValue()),rewardRuleMap,userHierarchyInfo);
			//7.3.二级扩展分红
			secondLevelExtendBonus2(order,new BigDecimal(order.getGfValue()),rewardRuleMap,userHierarchyInfo);
			//7.4.极差分红&加权分红，插入拆单表
			stageBonus2(order,new BigDecimal(order.getGfValue()),rewardRuleMap,userHierarchyInfo);
		}
		//8.结束前处理：回填DB，更新该条记录状态为已处理
		preFinishProcess2(order);

		//9. 转账处理，回填DB，更新订单状态  add by liangtengfei
		integerExchangeService.exChange2(order);

	}

	/**
	 * 处理育苗订单
	 *
	 * @param order
	 */
	/*
	public void processSeedOrder(KJNotice order){
		//1、计算获得订单总金额（单位分）
		BigDecimal orderAmount = order.getAmount();

		//2、获得股东升级规则
		Map<String, DdbShoreholderRule> shoreholderRuleMap = getShoreholderRuleMap();

		//3、该订单用户个人消费金额累计
		cumulateSelfSumAmountByUserid(order.getCustId(), orderAmount);
		//若为育苗类型：1 \ 2 \ 3
		//5、用户个人育苗金额累计
		cumulateGrowSeedAmountByUserid(order.getCustId(), orderAmount);
		//用户团队金额累计
		cumulateTeamSumAmountByUserid(order.getCustId(), orderAmount);

		//个人股东升级，满足则更新
		int newLevel = updateShareholderLevel(order.getCustId(), orderAmount, shoreholderRuleMap);
		int oldLevel = ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId()).getTreeNodeLevel();
		if(newLevel-oldLevel>0){
			//更新自己股东等级
			ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId()).setSelfStockholderLevel(newLevel);
			//逐级更新各个上级团队成员中各股东人数
			updateParentsTeamMemberCnt(order.getCustId(),oldLevel,newLevel);
			String newLevelStr = shoreholderRuleMap.get(newLevel+"").getLevelDefine();
			//更新DB
			updateShoreholder(order.getCustId(), order.getType(), newLevelStr);
			//保存股东流水表
			saveShoreholder(order, newLevelStr);
		}else {
			//更新DB
			String oldLevelStr = null;
			DdbShoreholderRule ddbShoreholderRule = shoreholderRuleMap.get(oldLevel+"");
			if(ddbShoreholderRule!=null){
				oldLevelStr = ddbShoreholderRule.getLevelDefine();
			}
			updateShoreholder(order.getCustId(),order.getType(), oldLevelStr);
		}
		 //
		 // 逐级股东升级：
		 // 		循环更新上级团队金额、股东升级、逐一更新各个上级股东级别个数、直接下级购买的特殊商品数量、更新DB及股东升级流水
		 //
		updateParentsCustomerInfo(order, shoreholderRuleMap);

		//99、结束前处理
		preFinishProcess(order);
	}
	*/
	/**
	 * 逐级股东升级：
	 * 循环更新上级团队金额、股东升级、逐一更新各个上级股东级别个数、直接下级购买的特殊商品数量、更新DB及股东升级流水
	 * 
	 * @param order
	 * @param shoreholderRuleMap
	 */
	public void updateParentsCustomerInfo(KJNotice order,Map<String, DdbShoreholderRule> shoreholderRuleMap){
		List<String> parentsList = ConstantDict.CUSTOMER_TREE_DATA.get(order.getCustId()).getParentsList();
		if(parentsList!=null&&parentsList.size()>0){
			// 团队业绩发送到mq并且入库，同一笔订单要求入库时间一样，所以生成一个公共时间
			String actionTime = DateUtils.getDateTime();
			for (int i = parentsList.size()-1; i >=0 ; i--) {
				String parentId = parentsList.get(i);
				CustomerInfo4LevelDiff customerInfo = ConstantDict.CUSTOMER_TREE_DATA.get(parentId);
				//1.累计团队金额（逐级上级股东）
				BigDecimal teamAmount = customerInfo.getTeamSumAmount()!=null?customerInfo.getTeamSumAmount():new BigDecimal(0);
				ConstantDict.CUSTOMER_TREE_DATA.get(parentId).setTeamSumAmount(order.getAmount().add(teamAmount));
				
				//2.累计直接下级购买特殊商品的数量（仅直接上级）
				int needAddDsubSpecialNum = 0;
				if(i == parentsList.size()-1 && order.getSpecialNum() > 0){
					needAddDsubSpecialNum = order.getSpecialNum();
					ConstantDict.CUSTOMER_TREE_DATA.get(parentId).setDsubSpecialNum(
							(needAddDsubSpecialNum + ConstantDict.CUSTOMER_TREE_DATA.get(parentId).getDsubSpecialNum()));
				}
				
				//3.股东升级：判断是否满足升级条件，若满足则触发升级
				checkAndUpgradeShareholderLevel(order, parentId, needAddDsubSpecialNum, shoreholderRuleMap);
				
				/*
				//3.满足股东升级条件则升级
				int newLevel = updateShareholderLevel(parentId, order.getAmount(), shoreholderRuleMap);
				int oldLevel = ConstantDict.CUSTOMER_TREE_DATA.get(parentId).getSelfStockholderLevel();
				
				if(newLevel-oldLevel>0){
					//4.更新股东级别
					ConstantDict.CUSTOMER_TREE_DATA.get(parentId).setSelfStockholderLevel(newLevel);
					//5.更新直接上级股东级别个数
					updateParentsTeamMemberCnt(parentId, oldLevel, newLevel);
					String newLevelStr = shoreholderRuleMap.get(newLevel+"").getLevelDefine();
					//6.更新DB
					updateShoreholder(parentId,order.getType(),newLevelStr,0,needAddDsubSpecialNum);
					//7.保存股东流水表
					saveShoreholder(parentId, newLevelStr, order);
					//8.股东升级后，根据新级别发放福利奖励
					upgradeBenefits(parentId, newLevelStr, order.getFlowId());
				}else {
					//6.更新DB
					String oldLevelStr = null;
					DdbShoreholderRule ddbShoreholderRule = shoreholderRuleMap.get(oldLevel+"");
					if(ddbShoreholderRule!=null){
						oldLevelStr = ddbShoreholderRule.getLevelDefine();
					}
					updateShoreholder(parentId,order.getType(),oldLevelStr,0,needAddDsubSpecialNum);
				}
				*/
				
				//9.把团队业绩信息  发送到 MQ  准备入库   liangtf 注释掉  待测试
				teamSumProductMQService.sendTeamSumToMq(parentId,order,actionTime);
			}
		}else {
			log.info("Update parent's CustomerInfo4LevelDiff complete");
		}
	}

	/**
	 * 查询直接上级，更新其团队成员中各股东级别的人数
	 *
	 * @param custId
	 * @param oldLevel
	 * @param newLevel
	 */
	public void updateParentsTeamMemberCnt(String custId, int oldLevel, int newLevel){
		//判断是否升级
		if(oldLevel==newLevel){
			return;
		}
		//查询用户缓存，判断是否有上级
		String parentId = customerMapper.getinviterByinviteeCustId(custId);
		if(parentId!=null&&!parentId.equals("")){
			//从用户缓存中，获取上级用户信息：团队成员中各个股级的人数teamMemberCntByStockholderLevel
			Map<String, Integer> teamMemberCntMap = ConstantDict.CUSTOMER_TREE_DATA.get(parentId).getTeamMemberCntByStockholderLevel();
			//升级前等级的人数若>0，则-1
			if (oldLevel>0 && teamMemberCntMap!=null
					&&teamMemberCntMap.get(oldLevel+"")>0) {
				teamMemberCntMap.put(String.valueOf(oldLevel), Integer.valueOf(teamMemberCntMap.get(oldLevel+"")-1));
			}
			//升级后等级的人数若为null或0则设1，否则+1
			if (newLevel>0) {
				if (teamMemberCntMap==null) {
					teamMemberCntMap = new HashMap<String, Integer>();
					teamMemberCntMap.put(String.valueOf(newLevel), new Integer(1));
					ConstantDict.CUSTOMER_TREE_DATA.get(parentId).setTeamMemberCntByStockholderLevel(teamMemberCntMap);
				}else if(teamMemberCntMap.get(newLevel+"")==null
						|| teamMemberCntMap.get(newLevel+"")==0){
					teamMemberCntMap.put(String.valueOf(newLevel), new Integer(1));
				}else {
					teamMemberCntMap.put(String.valueOf(newLevel), Integer.valueOf(teamMemberCntMap.get(newLevel+"")+1));
				}
			}
			updateShoreholderTeamMemberCnt(parentId, teamMemberCntMap);
		}
	}
	
	/**
	 * 单独更新团队各股东人数
	 * @param cust_id
	 * @param teamMemberCntMap
	 */
	public void updateShoreholderTeamMemberCnt(String cust_id, Map<String, Integer> teamMemberCntMap){
		if(teamMemberCntMap!=null&&teamMemberCntMap.size()>0){
			String teamMemberCnt = JSON.toJSONString(teamMemberCntMap);
			Map<String,Object> shoreholderMap = new HashMap<String,Object>();
			shoreholderMap.put("cust_id", cust_id);
			shoreholderMap.put("team_member_levels_cnt", teamMemberCnt);
			ddbShoreholderMapper.updateShoreholder(shoreholderMap);
		}
	}
	/**
	 * 结束前处理
	 * 		更新订单表状态
	 * 		更新分红订单流水表记录
	 */
	private void preFinishProcess(KJNotice order) {
		String flowId = "";
		if(order.getType().equals("1")) {
			//更新订单表状态
			flowId = order.getFlowId();
			ddbOrderMapper.updateStateByFlowId(1, flowId);

			/*
			 * 更新分红订单流水表记录
			 * 更新条件：
			 * 		分红对象ZID=order的flowId）
			 * 更新字段：
			 *  	状态=1已处理
			 *  	处理时间=系统当前时间
			 */
			ddbRewardProcessSeqMapper.updateByOrderZid(1,flowId);

		}else if(order.getType().equals("2")) {
			//更新育基地苗购买记录表状态
			flowId = order.getFlowId();
			ddbGrowSeedMapper.updateStateByZId(1,flowId);
			/*
			 * 更新分红订单流水表记录
			 * 更新条件：
			 * 		分红对象ZID=order的flowId
			 * 更新字段：
			 *  	状态=1已处理
			 *  	处理时间=系统当前时间
			 */
			ddbRewardProcessSeqMapper.updateByOrderZid(1,flowId);
		}
	}

	/**
	 * 结束前处理
	 * 		更新订单表状态
	 * 		更新分红订单流水表记录
	 */
	private void preFinishProcess2(KJNotice order) {
		String flowId = "";
		if(order.getType().equals("1")) {
			//更新订单表状态
			flowId = order.getFlowId();

			/*
			 * 更新分红订单流水表记录
			 * 更新条件：
			 * 		分红对象ZID=order的flowId）
			 * 更新字段：
			 *  	状态=1已处理
			 *  	处理时间=系统当前时间
			 */
			ddbRewardGfProcessSeqMapper.updateByOrderZid(1,flowId);
		}
	}

	private Map<String, Integer>  getOrderUserLower(String custId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		DdbShoreholder selectbyCustId = new DdbShoreholder();
		// 得到其下级的custID的集合
		List<String> getinviteeByOrderUserCustId = customerMapper.getinviteeByOrderUserCustId(custId);
//    	遍历，查询股东表，得到等级
		for(String userId : getinviteeByOrderUserCustId) {
			selectbyCustId = ddbShoreholderMapper.selectbyCustId(userId);
			String levelDefine = selectbyCustId.getLevelDefine();
			Integer num = map.get(levelDefine);
			if(num == null || num < 1) {
				map.put(levelDefine, 1);
			}else {
				map.put(levelDefine, num+1);
			}
		}
		return map;
	}

	/**
	 * 更新股东信息表
	 * @param custId	用户ID
	 * @param type	类型:1订单,2育苗购买,3新增注册
	 * @param level	股东等级
	 * @param needUpdateOwnSpecialNum	大于0时更新自己购买的特殊商品数量
	 * @param needUpdateDsubSpecialNum	大于0时更新直接下级购买的特殊商品数量
	 */
	private void updateShoreholder(String custId,String type,String level,int needUpdateOwnSpecialNum, int needUpdateDsubSpecialNum){

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cust_id", custId);
		map.put("level_define", level);
		map.put("sum_amount", ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfSumAmount()); //个人消费额
		if(type.equals("2")){
			map.put("seed_amount",ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfYumiaoSumAmount());//个人育苗额
		}
		/*if(ConstantDict.CUSTOMER_TREE_DATA.get(custId)
				.getTeamMemberCntByStockholderLevel()!=null
				&&ConstantDict.CUSTOMER_TREE_DATA.get(custId)
				.getTeamMemberCntByStockholderLevel().size()>0){
			Map<String,Integer> cntMap = null;
			cntMap = ConstantDict.CUSTOMER_TREE_DATA.get(custId)
					.getTeamMemberCntByStockholderLevel();
			map.put("team_member_levels_cnt", JSON.toJSONString(cntMap));
		}*/
		map.put("team_sum_amount", ConstantDict.CUSTOMER_TREE_DATA.get(custId).getTeamSumAmount());
		map.put("update_time", DateUtils.parseDate(DateUtils.getDateTime()));
		if(needUpdateOwnSpecialNum > 0){
			map.put("own_special_num", ConstantDict.CUSTOMER_TREE_DATA.get(custId).getOwnSpecialNum());
		}
		if(needUpdateDsubSpecialNum > 0){
			map.put("dsub_special_num", ConstantDict.CUSTOMER_TREE_DATA.get(custId).getDsubSpecialNum());
		}
		ddbShoreholderMapper.updateShoreholder(map);
	}
	/**
	 * 保存股东升级流水表
	 * @return
	 */
	private int saveShoreholder(String custId,String level,String flowId){
		DdbShoreholderUpgradeSeq ddbShoreholderUpgradeSeq = new DdbShoreholderUpgradeSeq();
		ddbShoreholderUpgradeSeq.setCustId(custId);
		ddbShoreholderUpgradeSeq.setOrderId(flowId);
		//获取当前时间
		Date date = DateUtils.parseDate(DateUtils.getDateTime());
		ddbShoreholderUpgradeSeq.setCreateTime(date);
		ddbShoreholderUpgradeSeq.setUpgradeTime(date);
		ddbShoreholderUpgradeSeq.setNewLevelDefine(level);
		return ddbShoreholderUpgradeSeqMapper.saveDdbShoreholderUpgradeSeq(ddbShoreholderUpgradeSeq);
	}

	/**
	 * 当前用户自己特定商品数量累计
	 * @param custId
	 * @param specialNum
	 */
	private void cumulateOwnSpecialNumByUserid(String custId,int specialNum){
		if(specialNum > 0){
			if(ConstantDict.CUSTOMER_TREE_DATA.get(custId)==null){
				CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
				ConstantDict.CUSTOMER_TREE_DATA.put(custId, customerInfo);
			}
			//累加当前用户自己特定商品数量
			ConstantDict.CUSTOMER_TREE_DATA.get(custId).setOwnSpecialNum((specialNum + ConstantDict.CUSTOMER_TREE_DATA.get(custId).getOwnSpecialNum()));
		}
	}

	/**
	 * 用户团队消费金额累计
	 * @param custId
	 * @param orderAmount
	 */
	private void cumulateTeamSumAmountByUserid(String custId,BigDecimal orderAmount){
		if(ConstantDict.CUSTOMER_TREE_DATA.get(custId)==null){
			CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
			ConstantDict.CUSTOMER_TREE_DATA.put(custId, customerInfo);
		}
		if (ConstantDict.CUSTOMER_TREE_DATA.get(custId).getTeamSumAmount()==null) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId).setTeamSumAmount(new BigDecimal(0));
		}
		if (orderAmount!=null && orderAmount.compareTo(new BigDecimal(0))>0) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId)
					.setTeamSumAmount(orderAmount.add(ConstantDict.CUSTOMER_TREE_DATA.get(custId).getTeamSumAmount()));
		}
	}
	/**
	 * 用户个人育苗消费总额
	 * @param custId
	 * @param orderAmount
	 */
	private void cumulateGrowSeedAmountByUserid(String custId,BigDecimal orderAmount){
		if(ConstantDict.CUSTOMER_TREE_DATA.get(custId)==null){
			CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
			ConstantDict.CUSTOMER_TREE_DATA.put(custId, customerInfo);
		}
		if (ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfYumiaoSumAmount()==null) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId).setSelfYumiaoSumAmount(new BigDecimal(0));
		}
		if (orderAmount!=null && orderAmount.compareTo(new BigDecimal(0))>0) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId)
					.setSelfYumiaoSumAmount(orderAmount.add(ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfYumiaoSumAmount()));
		}
	}
	
	/**
	 * 获取字符串类型股东升级规则内容
	 * @param key
	 * @param ruleStr
	 * @return
	 */
	public String getMapValue(String key, String ruleStr) {
		if (key == null || key.equals("") || ruleStr == null || ruleStr.equals("")) {
			return null;
		} else {
			Map ruleMap = (Map)JSON.parseObject(ruleStr);
			return ruleMap.containsKey(key) ? (String)ruleMap.get(key) : null;
		}
	}

	/**
	 * 股东升级：判断是否满足升级条件，若满足则触发升级
	 * 
	 * 1、根据当前股东级别，向上逐级判断是否满足各级升级条件，每次满足升级条件则触发升级处理（更新MAP、记录股东升级流水表、福利奖励）。
	 *    （用户可以从无级别或者低级别一下升到很高的级别，视为发生了多次升级事件，每次升级事件都要记录流水表，并下发福利奖励。）
	 * 2、若升级，则更新直接上级团队成员中各股东人数。
	 * 3、根据MAP，更新股东信息表。
	 * 
	 * @param order	订单
	 * @param custId	用户ID
	 * @param needUpdateDsubSpecialNum 大于0时更新直接下级购买的特殊商品数量
	 */
	private void checkAndUpgradeShareholderLevel(KJNotice order, String custId, int needUpdateDsubSpecialNum,
			Map<String, DdbShoreholderRule> shoreholderRuleMap) {

		// 用户团队消费额
		int oldLevel = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfStockholderLevel();
		BigDecimal user_team_amount = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getTeamSumAmount();
		int user_own_special_num = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getOwnSpecialNum();
		int user_dsub_special_num = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getDsubSpecialNum();
		// 用户直推下级股东个数
		Map<String, Integer> lowUserLevelCountMap = getOrderUserLower(custId);
		// 新股东等级，默认为原股东等级
		int newLevel = oldLevel;
		// 新股东等级名
		String newLevelDefine = "";

		// 新版升级条件KEY--自己购买特定盒数
		String own_special_num_key = "own_special_num";
		// 新版升级条件KEY--直接下级购买特定盒数
		String dsub_special_num_key = "dsub_special_num";
		// 新版升级条件KEY--团队业绩达到数额
		String team_amount_key = "team_amount";
		// 新版直推需要满足的等级
		String need_level_key = "need_level";
		// 新版直推需要满足相应等级的个数
		String need_count_key = "need_count";
		
		// 获得股东升级条件List，按等级有低到高排序
		List<DdbShoreholderRule> shoreholderRuleList = ddbShoreholderRuleMapper.selectAllRule();
		if (shoreholderRuleList != null && shoreholderRuleList.size() > 0) {
			// 1、从用户当前等级开始，向上逐级判断是否满足各级升级条件
			for (int i=0; i<shoreholderRuleList.size(); i++) {
				DdbShoreholderRule ddbShoreholderRule = shoreholderRuleList.get(i);

				if (ddbShoreholderRule.getLevelSeqNo() == oldLevel) {
					newLevelDefine = ddbShoreholderRule.getLevelDefine();

				// 从用户当前等级开始向上
				} else if (ddbShoreholderRule.getLevelSeqNo() > oldLevel) {
					// 解析升级条件，获得条件MAP
					String upgradeConditions = ddbShoreholderRule.getUpgrade_conditions();
					Map conditionsMap = stringToMap(upgradeConditions);
					
					// 升级条件--团队业绩达到数额
					BigDecimal conditon_team_amount = new BigDecimal(getStringValueByKeyWithDefault(team_amount_key, conditionsMap, "0"));
					// 升级条件--自己购买特定盒数
					int conditon_own_special_num = Integer.parseInt(getStringValueByKeyWithDefault(own_special_num_key, conditionsMap, "0"));
					// 升级条件--直接下级购买特定盒数
					int conditon_dsub_special_num = Integer.parseInt(getStringValueByKeyWithDefault(dsub_special_num_key, conditionsMap, "0"));
					// 升级条件--直推下级满足等级的个数
					int need_count = Integer.parseInt(getStringValueByKeyWithDefault(need_count_key, conditionsMap, "0"));
					String need_level = getStringValueByKeyWithDefault(need_level_key, conditionsMap, "0");
					//用户团队各股东数teamMemberCnt大于need_level的全加在一块
					int real_level_count =  getNewChildTeamMemberCnt(lowUserLevelCountMap, need_level,shoreholderRuleMap);
					
					// 满足升级条件，触发升级处理
					if (user_team_amount.compareTo(conditon_team_amount) >= 0
							&& user_own_special_num >= conditon_own_special_num
							&& user_dsub_special_num >= conditon_dsub_special_num
							&& real_level_count >= need_count) {
						newLevel = ddbShoreholderRule.getLevelSeqNo();
						newLevelDefine = ddbShoreholderRule.getLevelDefine();
						// 更新股东信息MAP
						ConstantDict.CUSTOMER_TREE_DATA.get(custId).setSelfStockholderLevel(newLevel);
						// 保存股东流水表
						saveShoreholder(custId, newLevelDefine, order.getFlowId());
						// 股东升级后，根据新级别发放福利奖励
						//upgradeBenefits(custId, newLevelDefine, order.getFlowId());
					} else {
						// 不满足升级条件，不再向上逐级判断
						break;
					}
				}
			}
		}

		// 2、更新直接上级团队成员中各股级人数
		if (newLevel > oldLevel) {
			updateParentsTeamMemberCnt(custId, oldLevel, newLevel);
		}
		
		// 3、更新股东信息表
		updateShoreholder(custId, order.getType(), newLevelDefine, order.getSpecialNum(), needUpdateDsubSpecialNum);
	}
	/**
	 * 字符串转MAP
	 * @param str
	 * @return
	 */
	public Map stringToMap(String str) {
		if (str == null || str.equals("")) {
			return null;
		} else {
			return (Map)JSON.parseObject(str);
		}
	}
	
	/**
	 * 根据key从map获取值，值为字符串。若key不存在则返回默认值defaultValue。
	 * @param key
	 * @param map
	 * @param defaultValue
	 * @return
	 */
	public String getStringValueByKeyWithDefault(String key, Map map, String defaultValue) {
		if (key == null || key.equals("") || map == null) {
			return defaultValue;
		} else {
			return map.containsKey(key) ? (String)map.get(key) : defaultValue;
		}
	}
	
	/**
	 * 计算直接下级用户的团队消费额的合计值
	 * 
	 * @param directTeamAmount
	 * @param parentId
	 * @return
	 */
	public int getDirectChildTeamAmountCount(int directTeamAmount,String parentId){
		List<String> directChildrenList = customerMapper.
				getInvitorsByInviteUserId(parentId);
		//满足子类团队额的子类个数
		int count = 0;
		for (String childId : directChildrenList) {
			BigDecimal teamAmount = ConstantDict.CUSTOMER_TREE_DATA
					.get(childId).getTeamSumAmount()!=null?ConstantDict.CUSTOMER_TREE_DATA
					.get(childId).getTeamSumAmount():new BigDecimal(0);
			if(teamAmount.compareTo(new BigDecimal(directTeamAmount))>=0){
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 根据用户订单数额，升级股东级别 改为动态获取股东等级个数，动态判断升级条件
	 *
	 * @param custId
	 * @param orderAmount
	 * @param shoreholderRuleMap
	 */
//	private int updateShareholderLevel(String custId, BigDecimal orderAmount,
//									   Map<String, DdbShoreholderRule> shoreholderRuleMap) {
//		// 用户升级后的股东级别
//		int level = 0;
//		List<DdbShoreholderRule> shoreholderRuleList = ddbShoreholderRuleMapper.selectAllRule();
//		if (shoreholderRuleList != null && shoreholderRuleList.size() > 0) {
//			String team_amount_key = "team_amount";
//			String own_special_num_key = "own_special_num";
//			String dsub_special_num_key = "dsub_special_num";
//			/**
//			 * 新版升级条件如下：
//			 *
//			 * 		1.自己购买特定盒数 ；
//			 * 		2.直接下级购买特定盒数；
//			 * 		3.团队业绩达到数额；
//			 *
//			 */
//			int levelSize = shoreholderRuleList.size();
//			// 用户团队消费额
//			BigDecimal real_team_amount = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getTeamSumAmount();
//			int real_own_special_num = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getOwnSpecialNum();
//			int real_dsub_special_num = ConstantDict.CUSTOMER_TREE_DATA.get(custId).getDsubSpecialNum();
//			for (int i = levelSize - 1; i >= 0; i--) {
//				DdbShoreholderRule ddbShoreholderRule = shoreholderRuleList.get(i);
//				String upgradeConditions = ddbShoreholderRule.getUpgrade_conditions();
//				//团队消费金额条件
//				BigDecimal team_amount = new BigDecimal(getMapValue(team_amount_key, upgradeConditions)!=null?getMapValue(team_amount_key, upgradeConditions):"0");
//				//自己需要购买特定商品的数量的条件
//				int need_own_special_num = Integer.parseInt(getMapValue(own_special_num_key,upgradeConditions)!=null?getMapValue(own_special_num_key, upgradeConditions):"0");
//				//直接下级需要购买特定商品的数量的条件
//				int need_dsub_special_num = Integer.parseInt(getMapValue(dsub_special_num_key,upgradeConditions)!=null?getMapValue(dsub_special_num_key, upgradeConditions):"0");
//				if (real_team_amount.compareTo(team_amount) >= 0
//						&& real_own_special_num >= need_own_special_num
//						&& real_dsub_special_num >= need_dsub_special_num) {
//					level = ddbShoreholderRule.getLevelSeqNo();
//					break;
//				}
//			}
//		}
//		return level;
//	}
	
	/**
	 * 获取teamMemberCnt中大于等于need_level的个数累加在一块
	 * @param lowUserLevelCountMap
	 * @param need_level
	 * @return
	 */
	public static int getNewChildTeamMemberCnt(Map<String, Integer> lowUserLevelCountMap,
			String need_level,Map<String, DdbShoreholderRule> shoreholderRuleMap){
		int count = 0;
		if(lowUserLevelCountMap!=null
				&&lowUserLevelCountMap.size()>0)
		for(String level : lowUserLevelCountMap.keySet()){
				if(null!=level&&!level.equals("null")&&!level.equals("")){
					int levelInt = shoreholderRuleMap.get(level).getLevelSeqNo();
					int need_level_int = shoreholderRuleMap.get(need_level).getLevelSeqNo();
					if(levelInt>=need_level_int){
						count += lowUserLevelCountMap.get(level);
					}
				}
				
			}
		return count;
	}
	
	private Map<String, DdbShoreholderRule> getShoreholderRuleMap() {
		Map<String, DdbShoreholderRule> map = new HashMap<String, DdbShoreholderRule>();
		List<DdbShoreholderRule> ruleAll = ddbShoreholderRuleMapper.selectAllRule();
		for(DdbShoreholderRule ddbShoreholderRule : ruleAll) {
			Integer levelSeqNo = ddbShoreholderRule.getLevelSeqNo();
			map.put(levelSeqNo.toString(), ddbShoreholderRule);
			map.put(ddbShoreholderRule.getLevelDefine(), ddbShoreholderRule);
		}
		return map;
	}

	/**
	 * 获得商品规则信息
	 * @param ruleId
	 * @return
	 */
	private DdbGoodsRule getGoodsRuleInfoByRuleId(String ruleId) {
		//1、根据输入的规则ID（ruleId），查询商品规则表，条件字段zid=ruleId，
		DdbGoodsRule selectByZid = ddbGoodsRuleMapper.selectByZidAndTime(ruleId);

		return selectByZid;
	}

	/**
	 * 用户个人消费金额累计
	 * @param custId
	 * @param orderAmount
	 */
	private void cumulateSelfSumAmountByUserid(String custId, BigDecimal orderAmount) {
		if(ConstantDict.CUSTOMER_TREE_DATA.get(custId)==null){
			CustomerInfo4LevelDiff customerInfo = new CustomerInfo4LevelDiff();
			ConstantDict.CUSTOMER_TREE_DATA.put(custId, customerInfo);
		}
		if (ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfSumAmount()==null) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId).setSelfSumAmount(new BigDecimal(0));
		}
		if (orderAmount!=null && orderAmount.compareTo(new BigDecimal(0))>0) {
			ConstantDict.CUSTOMER_TREE_DATA.get(custId)
					.setSelfSumAmount(orderAmount.add(ConstantDict.CUSTOMER_TREE_DATA.get(custId).getSelfSumAmount()));
		}
	}

	/**
	 * 组装分红规则Map
	 * @param fruleGroup
	 * @return
	 */
	private Map<String, RewardRuleInfo> makeRewardRuleMap(String fruleGroup) {
		//根据1中获取的分红规则(frule_group)，查询分红规则表及分红规则明细，条件字段frule_group，获得分红规则明细List。
		//解析分红规则表分红参数(reward_args)，根据reward_args中的real_name，查询总账表，条件字段real_name，获得业务id(zid)。
		//组装分红规则Map<rule_type, RewardRuleInfo>
		//com.jinglitong.wallet.ddbapi.model.logic.RewardRuleInfo
		//RewardRuleInfo包含：
		//                 规则类型rule_type、
		//               分红基数比例reward_rate、
		//             积分分红参数reward_argsMap<积分名称(real_name), 总账积分ZID(inte_zid)+":"+分红比例(rate)>、
		//       拓展分红比例reward_percent（规则类型rule_type=1or2时有值,3or4时为空）、
		//   分红比例明细Map<股东等级名称(shoreholder_level_define), 分红规则明细ZID+":"+分红比例(reward_percent)>（规则类型rule_type=4时有值,1or2or3时为空）

		// 当rule_type 为1 or 2  时，为1对1；  当rule_type 为3 or 4  时，为1对多，

		Map<String, RewardRuleInfo> map = new HashMap<String, RewardRuleInfo>();


		DdbIntegralAccount selectByRealName = new DdbIntegralAccount();
		RewardRuleInfo rewardRuleInfo = null;


		List<DdbRewardRule> rewardDetailRuleList = ddbRewardRuleMapper.selectByfruleGroup(fruleGroup);
		if(rewardDetailRuleList == null || rewardDetailRuleList.size() == 0) {
			return map;
		}

		for(DdbRewardRule ddbRewardRule : rewardDetailRuleList){
			//   		积分分红参数Map
			Map<String, String> rewardargsMap = new HashMap<String, String>();
			//   		分红比例明细Map
			Map<String, String> ruleDetailMap = new HashMap<String, String>();
			rewardRuleInfo = new RewardRuleInfo();
			Integer ruleType = ddbRewardRule.getRuleType() == null ? 0 : ddbRewardRule.getRuleType();
			String rewardArgs = ddbRewardRule.getRewardArgs();
			String[] rewardArgsList = rewardArgs.split(",");
			for(String str : rewardArgsList) {
				String[] strs = str.split(":");
				String realName = strs[0];
				String rate = strs[1];
				selectByRealName = ddbIntegralAccountMapper.selectByRealName(realName);
				String zid = selectByRealName.getZid();
				//   			积分分红参数reward_argsMap
				rewardargsMap.put(realName, zid + ":" + rate);
			}

			rewardRuleInfo.setRuleType(ruleType.toString());
			Integer rewardRate = ddbRewardRule.getRewardRate() ==  null ? 0 : ddbRewardRule.getRewardRate();
			rewardRuleInfo.setRewardRate(new BigDecimal(rewardRate));
			rewardRuleInfo.setRewardArgsMap(rewardargsMap);
			List<DdbRewardRuleDetail> details = ddbRewardRule.getDetails();
			// 因为 RewardPercent  1，2 有值，3，4为空      shoreholder_level_define    1，2 为空，3.4不为空
			for(DdbRewardRuleDetail ddbRewardRuleDetail : details) {
				Integer rewardPercent = ddbRewardRuleDetail.getRewardPercent();
				String shoreholderLevelDefine = ddbRewardRuleDetail.getShoreholderLevelDefine();
				String zid = ddbRewardRuleDetail.getZid();
				if(1 == ruleType || 2 == ruleType) {
					rewardRuleInfo.setRewardPercent(new BigDecimal(rewardPercent));
				}else {
					ruleDetailMap.put(shoreholderLevelDefine, zid + ":" +rewardPercent.toString() );
				}
			}
			rewardRuleInfo.setRuleDetailMap(ruleDetailMap);
			map.put(ruleType.toString(), rewardRuleInfo);
		}

		return map;
	}


	/**
	 * 组装分红规则Map
	 * @param fruleGroup
	 * @return
	 */
	private Map<String, RewardRuleInfo> makeRewardRuleMap2(String fruleGroup) {
		//根据1中获取的分红规则(frule_group)，查询分红规则表及分红规则明细，条件字段frule_group，获得分红规则明细List。
		//解析分红规则表分红参数(reward_args)，根据reward_args中的real_name，查询总账表，条件字段real_name，获得业务id(zid)。
		//组装分红规则Map<rule_type, RewardRuleInfo>
		//com.jinglitong.wallet.ddbapi.model.logic.RewardRuleInfo
		//RewardRuleInfo包含：
		//                 规则类型rule_type、
		//               分红基数比例reward_rate、
		//             积分分红参数reward_argsMap<积分名称(real_name), 总账积分ZID(inte_zid)+":"+分红比例(rate)>、
		//       拓展分红比例reward_percent（规则类型rule_type=1or2时有值,3or4时为空）、
		//   分红比例明细Map<股东等级名称(shoreholder_level_define), 分红规则明细ZID+":"+分红比例(reward_percent)>（规则类型rule_type=4时有值,1or2or3时为空）

		// 当rule_type 为1 or 2  时，为1对1；  当rule_type 为3 or 4  时，为1对多，

		Map<String, RewardRuleInfo> map = new HashMap<String, RewardRuleInfo>();


		DdbIntegralAccount selectByRealName = new DdbIntegralAccount();
		RewardRuleInfo rewardRuleInfo = null;


		List<DdbRewardGfRule> rewardGfDetailRuleList = ddbRewardGfRuleMapper.selectByfruleGroup(fruleGroup);
		if(rewardGfDetailRuleList == null || rewardGfDetailRuleList.size() == 0) {
			return map;
		}

		for(DdbRewardGfRule ddbRewardGfRule : rewardGfDetailRuleList){
			//   		积分分红参数Map
			Map<String, String> rewardargsGfMap = new HashMap<String, String>();
			//   		分红比例明细Map
			Map<String, String> ruleDetailMap = new HashMap<String, String>();
			rewardRuleInfo = new RewardRuleInfo();
			Integer ruleType = ddbRewardGfRule.getRuleType() == null ? 0 : ddbRewardGfRule.getRuleType();
			String rewardArgs = ddbRewardGfRule.getRewardArgs();
			String[] rewardArgsList = rewardArgs.split(",");
			for(String str : rewardArgsList) {
				String[] strs = str.split(":");
				String realName = strs[0];
				String rate = strs[1];
				selectByRealName = ddbIntegralAccountMapper.selectByRealName(realName);
				String zid = selectByRealName.getZid();
				//   			积分分红参数reward_argsMap
				rewardargsGfMap.put(realName, zid + ":" + rate);
			}

			rewardRuleInfo.setRuleType(ruleType.toString());
			Integer rewardRate = ddbRewardGfRule.getRewardRate() ==  null ? 0 : ddbRewardGfRule.getRewardRate();
			rewardRuleInfo.setRewardRate(new BigDecimal(rewardRate));
			rewardRuleInfo.setRewardArgsMap(rewardargsGfMap);
			List<DdbRewardGfRuleDetail> details = ddbRewardGfRule.getDetails();
			// 因为 RewardPercent  1，2 有值，3，4为空      shoreholder_level_define    1，2 为空，3.4不为空
			for(DdbRewardGfRuleDetail ddbRewardRuleDetail : details) {
				Integer rewardPercent = ddbRewardRuleDetail.getRewardPercent();
				String shoreholderLevelDefine = ddbRewardRuleDetail.getShoreholderLevelDefine();
				String zid = ddbRewardRuleDetail.getZid();
				if(1 == ruleType || 2 == ruleType) {
					rewardRuleInfo.setRewardPercent(new BigDecimal(rewardPercent));
				}else {
					ruleDetailMap.put(shoreholderLevelDefine, zid + ":" +rewardPercent.toString() );
				}
			}
			rewardRuleInfo.setRuleDetailMap(ruleDetailMap);
			map.put(ruleType.toString(), rewardRuleInfo);
		}

		return map;
	}



	Map<String, DdbShoreholderRule> ddbShoreholderRule ;
	
	/**
	 * 一级拓展分红
	 * @param order	订单信息
	 * @param rcAmount	订单RC合计值
	 * @param rewardRuleMap	分红规则MAP
	 * @param userHierarchyInfo	订单用户信息
	 */
	public void startLevelExtendBonus(KJNotice order,BigDecimal rcAmount,Map<String, RewardRuleInfo> rewardRuleMap,
									  CustomerInfo4LevelDiff userHierarchyInfo){
		ddbShoreholderRule = getShoreholderRuleMap();
		//用户的上级用户id列表，列表中的股东排序是以从高到低排列的。
		List<String> superiorlist = userHierarchyInfo.getParentsList();
		if(superiorlist != null && superiorlist.size() >= 1){
			//从最后一个下标取出用户，进行一级分红
			for(int i = superiorlist.size(); i > 0;i--){

				CustomerInfo4LevelDiff startShareholder = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));  //获取该股东信息

				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <= 0){
					//非股东不参与分红
					continue;
				}else if(startShareholder.getSelfStockholderLevel() > 0){
					String levelDefine = ddbShoreholderRule.get(startShareholder.getSelfStockholderLevel()+"").getLevelDefine();
					RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("1");
					//分红基数比例(%)
					BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
					Map<String, String> fruleIdMap = ddbRewardRuleDetailMapper.selectZidByRuleType(1);
					//该股东分得的分红总金额，按金额分红改为按RC值进行分红
					BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
					//计算该股东实际分得各种积分的数量
					Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
					for (Map.Entry<String, String> entry : rewardArgsMap.entrySet()) {

						String realName = entry.getKey().toString();
						String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
						String value = entry.getValue().toString();
						String[] strs = value.split(":");
						String inteZid = strs[0];  //总账id
						String rate = strs[1];
						BigDecimal realRate = (new BigDecimal(rate)).divide(new BigDecimal(100)); //分红比例(%)
						int amount = divied_amount.multiply(realRate).intValue(); //分红数量

						DdbSeplitOrderReward seplitOrderReward = new DdbSeplitOrderReward();
						seplitOrderReward.setZid(UuidUtil.getUUID());
						seplitOrderReward.setCustId(startShareholder.getUserid());
						seplitOrderReward.setFlowId(order.getFlowId());
						seplitOrderReward.setRuleId(order.getRuleId());
						seplitOrderReward.setFruleId(fruleIdMap.get("zid"));
						seplitOrderReward.setOrderId(order.getShopTrade());
						seplitOrderReward.setInteZid(inteZid);
						seplitOrderReward.setRealCname(realcName);
						seplitOrderReward.setRealName(realName);
						seplitOrderReward.setLevelDefine(levelDefine);
						seplitOrderReward.setAmount(amount);
						seplitOrderReward.setRewardPercent(rewardRuleInfo.getRewardPercent()+"%*"+rate+"%");
						seplitOrderReward.setState(0);
						seplitOrderReward.setCreateTime(DateUtils.getDateTime());
						//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
						ddbSeplitOrderRewardMapper.insertSelective(seplitOrderReward);
					}
					break;
				}
			}
		}
	}

	/**
	 * 一级拓展分红
	 * @param order	订单信息
	 * @param rcAmount	订单RC合计值
	 * @param rewardRuleMap	分红规则MAP
	 * @param userHierarchyInfo	订单用户信息
	 */
	public void startLevelExtendBonus2(KJNotice order,BigDecimal rcAmount,Map<String, RewardRuleInfo> rewardRuleMap,
									  CustomerInfo4LevelDiff userHierarchyInfo){
		ddbShoreholderRule = getShoreholderRuleMap();
		//用户的上级用户id列表，列表中的股东排序是以从高到低排列的。
		List<String> superiorlist = userHierarchyInfo.getParentsList();
		if(superiorlist != null && superiorlist.size() >= 1){
			//从最后一个下标取出用户，进行一级分红
			for(int i = superiorlist.size(); i > 0;i--){

				CustomerInfo4LevelDiff startShareholder = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));  //获取该股东信息

				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <= 0){
					//普通用户不分红
					continue;
				}else if(startShareholder.getSelfStockholderLevel() > 0){
					String levelDefine = ddbShoreholderRule.get(startShareholder.getSelfStockholderLevel()+"").getLevelDefine();
					RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("1");
					//分红基数比例(%)
					BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
					Map<String, String> fruleIdMap = ddbRewardGfRuleDetailMapper.selectZidByRuleType(1);
					//该股东分得的分红总金额，按金额分红改为按RC值进行分红
					BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
					//计算该股东实际分得各种积分的数量
					Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
					for (Map.Entry<String, String> entry : rewardArgsMap.entrySet()) {

						String realName = entry.getKey().toString();
						String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
						String value = entry.getValue().toString();
						String[] strs = value.split(":");
						String inteZid = strs[0];  //总账id
						String rate = strs[1];
						BigDecimal realRate = (new BigDecimal(rate)).divide(new BigDecimal(100)); //分红比例(%)
						int amount = divied_amount.multiply(realRate).intValue(); //分红数量

						DdbSeplitOrderRewardGf seplitOrderRewardGf = new DdbSeplitOrderRewardGf();
						seplitOrderRewardGf.setZid(UuidUtil.getUUID());
						seplitOrderRewardGf.setCustId(startShareholder.getUserid());
						seplitOrderRewardGf.setFlowId(order.getFlowId());
						seplitOrderRewardGf.setRuleId(order.getRuleId());
						seplitOrderRewardGf.setFruleId(fruleIdMap.get("zid"));
						seplitOrderRewardGf.setOrderId(order.getShopTrade());
						seplitOrderRewardGf.setInteZid(inteZid);
						seplitOrderRewardGf.setRealCname(realcName);
						seplitOrderRewardGf.setRealName(realName);
						seplitOrderRewardGf.setLevelDefine(levelDefine);
						seplitOrderRewardGf.setAmount(amount);
						seplitOrderRewardGf.setRewardPercent(rewardRuleInfo.getRewardPercent()+"%*"+rate+"%");
						seplitOrderRewardGf.setState(0);
						seplitOrderRewardGf.setCreateTime(DateUtils.getDateTime());
						//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
						ddbSeplitOrderRewardGfMapper.insertSelective(seplitOrderRewardGf);
					}
					break;
				}
			}
		}
	}

	/**
	 * 二级拓展分红
	 * @param order	订单信息
	 * @param orderAmount	订单金额
	 * @param userHierarchyInfo	订单用户信息
	 */
	public void secondLevelExtendBonus(KJNotice order,BigDecimal orderAmount,List<SecExpandDicInfo> rewardRuleList,
									   CustomerInfo4LevelDiff userHierarchyInfo){
		ddbShoreholderRule = getShoreholderRuleMap();
		//用户的上级用户id列表，列表中的股东排序是以从高到低排列的。
		List<String> superiorlist = userHierarchyInfo.getParentsList();
		if(order.getSpecialNum()>0&&superiorlist != null && superiorlist.size() >= 2){
			int goon = 0;
			//从最后一个下标取出用户，进行一级分红
			for(int i = superiorlist.size(); i > 0;i--){
				// 先排除直接分红的用户
				// 取完成直接分红用户之后的用户，进行二级分红
				CustomerInfo4LevelDiff startShareholder = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));  //获取该股东信息
				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <= 0){
					continue;
				}else {
					if(startShareholder.getSelfStockholderLevel()>0 && goon==0){
						goon++;
						continue;
					}else if(goon>=1 && startShareholder.getSelfStockholderLevel()>0){
						String levelDefine = ddbShoreholderRule.get(startShareholder.getSelfStockholderLevel()+"").getLevelDefine();
						//RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("2");
//						BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
//						Map<String, String> fruleIdMap = ddbRewardRuleDetailMapper.selectZidByRuleType(2);
						//该股东分得的总金额
//						BigDecimal divied_amount = orderAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
						//计算该股东实际分得奖励的数量
//						Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();

						for (SecExpandDicInfo entry : rewardRuleList) {
//							String realName = entry.getRealName();
//							String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
//							String value = entry.getValue().toString();
//							String[] strs = value.split(":");
//							String inteZid = strs[0];  //总账id
//							String rate = strs[1];
//							BigDecimal realRate = (new BigDecimal(rate)).divide(new BigDecimal(100)); //分红比例
//							int amount = divied_amount.multiply(realRate).intValue(); //分红数量
							int amount =entry.getInteAmount()*Integer.parseInt(order.getProductNum());
							DdbSeplitOrderReward seplitOrderReward = new DdbSeplitOrderReward();
							seplitOrderReward.setZid(UuidUtil.getUUID());
							seplitOrderReward.setCustId(startShareholder.getUserid());
							seplitOrderReward.setFlowId(order.getFlowId());
							seplitOrderReward.setRuleId(order.getRuleId());
							seplitOrderReward.setFruleId(entry.getZid());
							seplitOrderReward.setOrderId(order.getShopTrade());
							seplitOrderReward.setInteZid(entry.getInteId());
							seplitOrderReward.setRealCname(entry.getRealCname());
							seplitOrderReward.setRealName(entry.getRealName());
							seplitOrderReward.setLevelDefine(levelDefine);
//							seplitOrderReward.setRewardPercent(rewardRuleInfo.getRewardPercent()+"%*"+rate+"%");
							seplitOrderReward.setAmount(amount);
							seplitOrderReward.setState(0);
							seplitOrderReward.setCreateTime(DateUtils.getDateTime());
							seplitOrderReward.setType(1);
							//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
							ddbSeplitOrderRewardMapper.insertSelective(seplitOrderReward);
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * 二级拓展分红
	 * @param order	订单信息
	 * @param rewardRuleMap	分红规则LIST
	 * @param userHierarchyInfo	订单用户信息
	 */
	public void secondLevelExtendBonus2(KJNotice order,BigDecimal rcAmount,Map<String, RewardRuleInfo> rewardRuleMap,
                                        CustomerInfo4LevelDiff userHierarchyInfo){
        ddbShoreholderRule = getShoreholderRuleMap();
        //用户的上级用户id列表，列表中的股东排序是以从高到低排列的。
        List<String> superiorlist = userHierarchyInfo.getParentsList();
        if(superiorlist != null && superiorlist.size() >= 2){
			int goon = 0;
			//从最后一个下标取出用户，进行一级分红
            for(int i = superiorlist.size(); i > 0;i--){
                CustomerInfo4LevelDiff startShareholder = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));  //获取该股东信息

				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <=0){
					continue;
				}else {
					if(goon==0){
						goon++;
						continue;
					}else if(goon>=1 && startShareholder.getSelfStockholderLevel()>0){
						String levelDefine = ddbShoreholderRule.get(startShareholder.getSelfStockholderLevel()+"").getLevelDefine();
						RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("2");
						//分红基数比例(%)
						BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
						Map<String, String> fruleIdMap = ddbRewardGfRuleDetailMapper.selectZidByRuleType(2);
						//该股东分得的分红总金额，按金额分红改为按RC值进行分红
						BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
						//计算该股东实际分得各种积分的数量
						Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
						for (Map.Entry<String, String> entry : rewardArgsMap.entrySet()) {

							String realName = entry.getKey().toString();
							String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
							String value = entry.getValue().toString();
							String[] strs = value.split(":");
							String inteZid = strs[0];  //总账id
							String rate = strs[1];
							BigDecimal realRate = (new BigDecimal(rate)).divide(new BigDecimal(100)); //分红比例(%)
							int amount = divied_amount.multiply(realRate).intValue(); //分红数量

							DdbSeplitOrderRewardGf seplitOrderRewardGf = new DdbSeplitOrderRewardGf();
							seplitOrderRewardGf.setZid(UuidUtil.getUUID());
							seplitOrderRewardGf.setCustId(startShareholder.getUserid());
							seplitOrderRewardGf.setFlowId(order.getFlowId());
							seplitOrderRewardGf.setRuleId(order.getRuleId());
							seplitOrderRewardGf.setFruleId(fruleIdMap.get("zid"));
							seplitOrderRewardGf.setOrderId(order.getShopTrade());
							seplitOrderRewardGf.setInteZid(inteZid);
							seplitOrderRewardGf.setRealCname(realcName);
							seplitOrderRewardGf.setRealName(realName);
							seplitOrderRewardGf.setLevelDefine(levelDefine);
							seplitOrderRewardGf.setAmount(amount);
							seplitOrderRewardGf.setRewardPercent(rewardRuleInfo.getRewardPercent()+"%*"+rate+"%");
							seplitOrderRewardGf.setState(0);
							seplitOrderRewardGf.setCreateTime(DateUtils.getDateTime());
							//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
							ddbSeplitOrderRewardGfMapper.insertSelective(seplitOrderRewardGf);
						}
						break;
					}
				}

            }
        }
	}

	/**
	 * 级差分红
	 * @param order	订单
	 * @param rcAmount	订单RC合计值
	 * @param rewardRuleMap	分红规则MAP
	 * @param userHierarchyInfo	订单用户信息
	 */
	public void stageBonus(KJNotice order,BigDecimal rcAmount,Map<String, RewardRuleInfo> rewardRuleMap,
						   CustomerInfo4LevelDiff userHierarchyInfo){
		ddbShoreholderRule = getShoreholderRuleMap();

		Map<String,String> userToBonus = new HashMap<>();  //可以进行级差分红的用户id
		Map<String,List<String>> userToBonus2 = new HashMap<>();  //可以进行加权分红的用户id

		//获取该订单用户的上级股东idlist。
		List<String> superiorlist = userHierarchyInfo.getParentsList();

		if(superiorlist != null && superiorlist.size() > 0){
			//判断每个股东的等级(从直接上级开始逐级向上遍历)
			for(int i = superiorlist.size() ; i > 0; i--){
				boolean isMax = true;
				CustomerInfo4LevelDiff userInfo = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));
				//直接邀请人若等级大于0，则直接插入map中。
				if(userInfo.getSelfStockholderLevel() > 0 && userToBonus.get(""+userInfo.getSelfStockholderLevel()) == null ){
					if(userToBonus.size() > 0){
						//判断是否小于已在map中的key值，若小于，则去参加加权分红，若均大于map中已存在的key，则参加级差分红。
						for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
							if(userInfo.getSelfStockholderLevel() < Integer.parseInt(userMap.getKey())){
								isMax = false;
								break;
							};
						}
						if(isMax){
							userToBonus.put(""+userInfo.getSelfStockholderLevel(), userInfo.getUserid());
						}else if(!isMax && userInfo.getSelfStockholderLevel() > 1){
							if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
								List<String> userList = new ArrayList<>();
								userList.add(userInfo.getUserid());
								userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
								continue;
							}else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
								userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(userInfo.getUserid());
								continue;
							}
						}
					}
					userToBonus.put(""+userInfo.getSelfStockholderLevel(), userInfo.getUserid());
				}else if(userInfo.getSelfStockholderLevel() > 1 && userToBonus.get(""+userInfo.getSelfStockholderLevel()) != null){
					if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
						List<String> userList = new ArrayList<>();
						userList.add(userInfo.getUserid());
						userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
					}else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
						userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(userInfo.getUserid());
					}
				}
			}

			//查询参与级差分红的股东层级数
			List<String> levelNameList = ddbRewardRuleDetailMapper.selectLevelNum(3);
			//判断参与级差分红的map数据阶段，匹配相应的分红规则
			StringBuilder bonusSystem = new StringBuilder();
			for(int a = 0;a < levelNameList.size();a++){
				int levelNo = ddbShoreholderRule.get(levelNameList.get(a)).getLevelSeqNo();
				if(userToBonus.get(levelNo+"") != null){
					bonusSystem.append("1");
				}else{
					bonusSystem.append("0");
				};
			}
			//极差分红开始=============
			if(ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()) != null){
				//循环参与级差分红的用户信息
				for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
					if(ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()).get(userMap.getKey()) != null) {
						String levelDefine = ddbShoreholderRule.get(userMap.getKey()).getLevelDefine();
						String bnousInfo = ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()).get(userMap.getKey());
						String[] strs = bnousInfo.split("_");
						String fruleId = strs[0];   //该级股东级别在分红规则明细表中的zid
						String rate = strs[1]; //该级股东级别对应的分红比例

						RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("3");
						BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
						//该股东分得的总金额
						BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(new BigDecimal(rate)).divide(new BigDecimal(100));

						//计算该股东实际分得各个积分的数量
						Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
						//循环分红所得的各种奖励，计算按多少比例发放，插入查单表
						for (Map.Entry<String, String> userMap1 : rewardArgsMap.entrySet()) {
							String realName = userMap1.getKey().toString();
							String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
							String value = userMap1.getValue().toString();
							String[] strs1 = value.split(":");
							String inteZid1 = strs1[0];  //总账id
							String rate1 = strs1[1]; //所占分红比例 如20%
							BigDecimal realRate = (new BigDecimal(rate1)).divide(new BigDecimal(100));
							int amount = divied_amount.multiply(realRate).intValue(); //分红数量
							//拼装数据
							DdbSeplitOrderReward seplitOrderReward = new DdbSeplitOrderReward();
							seplitOrderReward.setZid(UuidUtil.getUUID());
							seplitOrderReward.setCustId(userMap.getValue().toString());
							seplitOrderReward.setFlowId(order.getFlowId());
							seplitOrderReward.setRuleId(order.getRuleId());
							seplitOrderReward.setFruleId(fruleId);
							seplitOrderReward.setOrderId(order.getShopTrade());
							seplitOrderReward.setInteZid(inteZid1);
							seplitOrderReward.setRealCname(realcName);
							seplitOrderReward.setRealName(realName);
							seplitOrderReward.setLevelDefine(levelDefine);
							seplitOrderReward.setRewardPercent(rate+"%*"+rate1+"%");
							seplitOrderReward.setAmount(amount);
							seplitOrderReward.setState(0);
							seplitOrderReward.setCreateTime(DateUtils.getDateTime());
							//插入拆单表
							//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
							ddbSeplitOrderRewardMapper.insertSelective(seplitOrderReward);
						}
					}
				}
			}
			//级差分红结束===========

			//5.加权分红，插入拆单表
			//查询参与加权分红的股东层级数
			List<String> levelNumList2 = ddbRewardRuleDetailMapper.selectLevelNum(4);
			//获取加权分红的分红规则信息
			RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("4");
			//获取加权分红的分红基数
			BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
			//获取包含六个不同股东层级分红比例信息的map
			Map<String, String> ruleDetailMap = rewardRuleInfo.getRuleDetailMap();
			
			
			
			//处理用户信息
			for(int b = 0;b < levelNumList2.size();b++){
				String levelDefine = ddbShoreholderRule.get(levelNumList2.get(b)).getLevelDefine();
				int levelNo = ddbShoreholderRule.get(levelNumList2.get(b)).getLevelSeqNo();
				if(userToBonus2.get(""+levelNo) != null && ruleDetailMap.get(levelDefine) != null){
					int userSize = userToBonus2.get(""+levelNo).size();  //参与该级别加权分红的用户数量
					String value = ruleDetailMap.get(levelDefine);
					String[] strs = value.split(":");
					String bonusZid = strs[0];  //分红规则明细Zid
					String rate = strs[1]; //分红比例
					//每个股东分得的金额
					BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(new BigDecimal(rate))
							.divide(new BigDecimal(100)).divide(new BigDecimal(userSize),3,BigDecimal.ROUND_HALF_DOWN);
					for(String custmer_id:userToBonus2.get(""+levelNo)){
						//计算该股东实际分得的各种积分数量
						Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
						//循环分红所得的各种奖励，计算按多少比例发放，插入查单表
						for (Map.Entry<String, String> userMap1 : rewardArgsMap.entrySet()) {
							String realName4 = userMap1.getKey().toString();
							String realcName4 = ddbIntegralAccountMapper.selectByRealName(realName4).getRealCname();
							String value4 = userMap1.getValue().toString();
							String[] strs4 = value4.split(":");
							String inteZid4 = strs4[0];  //总账id
							String rate4 = strs4[1]; //所占分红比例 如20%
							BigDecimal realRate = (new BigDecimal(rate4)).divide(new BigDecimal(100));
							int amount = divied_amount.multiply(realRate).intValue(); //分红数量
							//拼装数据
							DdbSeplitOrderReward seplitOrderReward = new DdbSeplitOrderReward();
							seplitOrderReward.setZid(UuidUtil.getUUID());
							seplitOrderReward.setCustId(custmer_id);
							seplitOrderReward.setFlowId(order.getFlowId());
							seplitOrderReward.setRuleId(order.getRuleId());
							seplitOrderReward.setFruleId(bonusZid);
							seplitOrderReward.setOrderId(order.getShopTrade());
							seplitOrderReward.setInteZid(inteZid4);
							seplitOrderReward.setRealCname(realcName4);
							seplitOrderReward.setRealName(realName4);
							seplitOrderReward.setLevelDefine(levelDefine);
							seplitOrderReward.setRewardPercent(rate+"%/"+userSize+"*"+rate4+"%");
							seplitOrderReward.setAmount(amount);
							seplitOrderReward.setState(0);
							seplitOrderReward.setCreateTime(DateUtils.getDateTime());
							//插入拆单表
							//ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
							ddbSeplitOrderRewardMapper.insertSelective(seplitOrderReward);
						}
					}
				}
			}
		}
	}


    /**
     * 级差分红
     * @param order	订单
     * @param rcAmount	订单RC合计值
     * @param rewardRuleMap	分红规则MAP
     * @param userHierarchyInfo	订单用户信息
     */
    public void stageBonus2(KJNotice order,BigDecimal rcAmount,Map<String, RewardRuleInfo> rewardRuleMap,
                           CustomerInfo4LevelDiff userHierarchyInfo){
        ddbShoreholderRule = getShoreholderRuleMap();

        Map<String,String> userToBonus = new HashMap<>();  //可以进行级差分红的用户id
        Map<String,List<String>> userToBonus2 = new HashMap<>();  //可以进行加权分红的用户id

        //获取该订单用户的上级股东idlist。
        List<String> superiorlist = userHierarchyInfo.getParentsList();

        if(superiorlist != null && superiorlist.size() > 0){
            //判断每个股东的等级(从直接上级开始逐级向上遍历)
            for(int i = superiorlist.size() ; i > 0; i--){
                boolean isMax = true;
                CustomerInfo4LevelDiff userInfo = ConstantDict.CUSTOMER_TREE_DATA.get(superiorlist.get(i-1));
                //直接邀请人若等级大于0，则直接插入map中。
                if(userInfo.getSelfStockholderLevel() > 0 && userToBonus.get(""+userInfo.getSelfStockholderLevel()) == null ){
                	boolean putongLessBuyerLevel = false;
                    if(userToBonus.size() > 0){
                        //判断是否小于已在map中的key值，若小于，则去参加加权分红，若均大于map中已存在的key，则参加级差分红。
                        for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
                            if(userInfo.getSelfStockholderLevel() < Integer.parseInt(userMap.getKey())){
                                isMax = false;
                                break;
                            };
                        }
                        if(isMax){
                            userToBonus.put(""+userInfo.getSelfStockholderLevel(), userInfo.getUserid());
                        }else if(!isMax && userInfo.getSelfStockholderLevel() > 1){
                            if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
                                List<String> userList = new ArrayList<>();
                                userList.add(userInfo.getUserid());
                                userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
                                continue;
                            }else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
                                userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(userInfo.getUserid());
                                continue;
                            }
                        }else if(!isMax && userInfo.getSelfStockholderLevel() == 1){
							putongLessBuyerLevel=true;
						}
                    }
                    if(!putongLessBuyerLevel){
						userToBonus.put(""+userInfo.getSelfStockholderLevel(), userInfo.getUserid());
					}
                }else if(userInfo.getSelfStockholderLevel() > 1 && userToBonus.get(""+userInfo.getSelfStockholderLevel()) != null){
                    if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
                        List<String> userList = new ArrayList<>();
                        userList.add(userInfo.getUserid());
                        userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
                    }else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
                        userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(userInfo.getUserid());
                    }
                }
            }

            //查询参与级差分红的股东层级数
            List<String> levelNameList = ddbRewardGfRuleDetailMapper.selectLevelNum(3);
            //判断参与级差分红的map数据阶段，匹配相应的分红规则
            StringBuilder bonusSystem = new StringBuilder();
            for(int a = 0;a < levelNameList.size();a++){
                int levelNo = ddbShoreholderRule.get(levelNameList.get(a)).getLevelSeqNo();
                if(userToBonus.get(levelNo+"") != null){
                    bonusSystem.append("1");
                }else{
                    bonusSystem.append("0");
                };
            }
            //极差分红开始=============
            if(ConstantDict.INIT_BONUS_SYSTEM_GF.get(bonusSystem.toString()) != null){
                //循环参与级差分红的用户信息
                for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
                    if(ConstantDict.INIT_BONUS_SYSTEM_GF.get(bonusSystem.toString()).get(userMap.getKey()) != null) {
                        String levelDefine = ddbShoreholderRule.get(userMap.getKey()).getLevelDefine();
                        String bnousInfo = ConstantDict.INIT_BONUS_SYSTEM_GF.get(bonusSystem.toString()).get(userMap.getKey());
                        String[] strs = bnousInfo.split("_");
                        String fruleId = strs[0];   //该级股东级别在分红规则明细表中的zid
                        String rate = strs[1]; //该级股东级别对应的分红比例

                        RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("3");
                        BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
                        //该股东分得的总金额
                        BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(new BigDecimal(rate)).divide(new BigDecimal(100));

                        //计算该股东实际分得各个积分的数量
                        Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
                        //循环分红所得的各种奖励，计算按多少比例发放，插入查单表
                        for (Map.Entry<String, String> userMap1 : rewardArgsMap.entrySet()) {
                            String realName = userMap1.getKey().toString();
                            String realcName = ddbIntegralAccountMapper.selectByRealName(realName).getRealCname();
                            String value = userMap1.getValue().toString();
                            String[] strs1 = value.split(":");
                            String inteZid1 = strs1[0];  //总账id
                            String rate1 = strs1[1]; //所占分红比例 如20%
                            BigDecimal realRate = (new BigDecimal(rate1)).divide(new BigDecimal(100));
                            int amount = divied_amount.multiply(realRate).intValue(); //分红数量
                            //拼装数据
                            DdbSeplitOrderRewardGf seplitOrderRewardGf = new DdbSeplitOrderRewardGf();
                            seplitOrderRewardGf.setZid(UuidUtil.getUUID());
                            seplitOrderRewardGf.setCustId(userMap.getValue().toString());
                            seplitOrderRewardGf.setFlowId(order.getFlowId());
                            seplitOrderRewardGf.setRuleId(order.getRuleId());
                            seplitOrderRewardGf.setFruleId(fruleId);
                            seplitOrderRewardGf.setOrderId(order.getShopTrade());
                            seplitOrderRewardGf.setInteZid(inteZid1);
                            seplitOrderRewardGf.setRealCname(realcName);
                            seplitOrderRewardGf.setRealName(realName);
                            seplitOrderRewardGf.setLevelDefine(levelDefine);
                            seplitOrderRewardGf.setRewardPercent(rate+"%*"+rate1+"%");
                            seplitOrderRewardGf.setAmount(amount);
                            seplitOrderRewardGf.setState(0);
                            seplitOrderRewardGf.setCreateTime(DateUtils.getDateTime());
                            //插入拆单表
                            //ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
                            ddbSeplitOrderRewardGfMapper.insertSelective(seplitOrderRewardGf);
                        }
                    }
                }
            }
            //级差分红结束===========

            //5.加权分红，插入拆单表
            //查询参与加权分红的股东层级数
            List<String> levelNumList2 = ddbRewardGfRuleDetailMapper.selectLevelNum(4);
            //获取加权分红的分红规则信息
            RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("4");
            //获取加权分红的分红基数
            BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
            //获取包含六个不同股东层级分红比例信息的map
            Map<String, String> ruleDetailMap = rewardRuleInfo.getRuleDetailMap();



            //处理用户信息
            for(int b = 0;b < levelNumList2.size();b++){
                String levelDefine = ddbShoreholderRule.get(levelNumList2.get(b)).getLevelDefine();
                int levelNo = ddbShoreholderRule.get(levelNumList2.get(b)).getLevelSeqNo();
                if(userToBonus2.get(""+levelNo) != null && ruleDetailMap.get(levelDefine) != null){
                    int userSize = userToBonus2.get(""+levelNo).size();  //参与该级别加权分红的用户数量
                    String value = ruleDetailMap.get(levelDefine);
                    String[] strs = value.split(":");
                    String bonusZid = strs[0];  //分红规则明细Zid
                    String rate = strs[1]; //分红比例
                    //每个股东分得的金额
                    BigDecimal divied_amount = rcAmount.multiply(rewardRate).multiply(new BigDecimal(rate))
                            .divide(new BigDecimal(100)).divide(new BigDecimal(userSize),3,BigDecimal.ROUND_HALF_DOWN);
                    for(String custmer_id:userToBonus2.get(""+levelNo)){
                        //计算该股东实际分得的各种积分数量
                        Map<String, String> rewardArgsMap = rewardRuleInfo.getRewardArgsMap();
                        //循环分红所得的各种奖励，计算按多少比例发放，插入查单表
                        for (Map.Entry<String, String> userMap1 : rewardArgsMap.entrySet()) {
                            String realName4 = userMap1.getKey().toString();
                            String realcName4 = ddbIntegralAccountMapper.selectByRealName(realName4).getRealCname();
                            String value4 = userMap1.getValue().toString();
                            String[] strs4 = value4.split(":");
                            String inteZid4 = strs4[0];  //总账id
                            String rate4 = strs4[1]; //所占分红比例 如20%
                            BigDecimal realRate = (new BigDecimal(rate4)).divide(new BigDecimal(100));
                            int amount = divied_amount.multiply(realRate).intValue(); //分红数量
                            //拼装数据
                            DdbSeplitOrderRewardGf seplitOrderRewardGf = new DdbSeplitOrderRewardGf();
                            seplitOrderRewardGf.setZid(UuidUtil.getUUID());
                            seplitOrderRewardGf.setCustId(custmer_id);
                            seplitOrderRewardGf.setFlowId(order.getFlowId());
                            seplitOrderRewardGf.setRuleId(order.getRuleId());
                            seplitOrderRewardGf.setFruleId(bonusZid);
                            seplitOrderRewardGf.setOrderId(order.getShopTrade());
                            seplitOrderRewardGf.setInteZid(inteZid4);
                            seplitOrderRewardGf.setRealCname(realcName4);
                            seplitOrderRewardGf.setRealName(realName4);
                            seplitOrderRewardGf.setLevelDefine(levelDefine);
                            seplitOrderRewardGf.setRewardPercent(rate+"%/"+userSize+"*"+rate4+"%");
                            seplitOrderRewardGf.setAmount(amount);
                            seplitOrderRewardGf.setState(0);
                            seplitOrderRewardGf.setCreateTime(DateUtils.getDateTime());
                            //插入拆单表
                            //ddbSeplitOrderMapper.insertBonusRecord(seplitOrder);
                            ddbSeplitOrderRewardGfMapper.insertSelective(seplitOrderRewardGf);
                        }
                    }
                }
            }
        }
    }

	/**
	 * 升级福利赠送
	 * @param custId 用户id
	 * @param level 用户升级到的星级 onestar、twostart等
	 * @param flowId 订单flowId
	 */
	public void upgradeBenefits(String custId,String level,String flowId){

		 Weekend<DdbLevelupWelfareRule> weekend = Weekend.of(DdbLevelupWelfareRule.class);
	     WeekendCriteria<DdbLevelupWelfareRule, Object> criteria = weekend.weekendCriteria();
	     criteria.andEqualTo(DdbLevelupWelfareRule::getLevelupDefine,level);	        
	     List<DdbLevelupWelfareRule> list = ddbLevelupWelfareRuleMapper.selectByExample(weekend);
		
	     for(int i = 0; i < list.size(); i ++){
			DdbLevelupWelfareRule ddbLevelupWelfareRule = list.get(i);						
			//到总帐表找到对应的总账账户
			Weekend<DdbIntegralAccount> weekendAccount = Weekend.of(DdbIntegralAccount.class);
		    WeekendCriteria<DdbIntegralAccount, Object> criteriaAccount = weekendAccount.weekendCriteria();
		    criteriaAccount.andEqualTo(DdbIntegralAccount::getZid,ddbLevelupWelfareRule.getInteId());	        
		    DdbIntegralAccount account = ddbIntegralAccountMapper.selectOneByExample(weekendAccount);
		    if(account == null){
		    	throw new RuntimeException("总账账户不存在" + ddbLevelupWelfareRule.getInteId());
		    }
		    integerExchangeService.transferAccounts(ddbLevelupWelfareRule.getInteAmount(), custId, ddbLevelupWelfareRule.getInteId(), flowId);		    
			
			DdbLevelupWelfareRecord ddbLevelupWelfareRecord = new DdbLevelupWelfareRecord();
			ddbLevelupWelfareRecord.setZid(UuidUtil.getUUID());
			ddbLevelupWelfareRecord.setAmount(ddbLevelupWelfareRule.getInteAmount());
			ddbLevelupWelfareRecord.setCustId(custId);
			ddbLevelupWelfareRecord.setInteZid(ddbLevelupWelfareRule.getInteId());
			ddbLevelupWelfareRecord.setIntegName(account.getIntegName());
			ddbLevelupWelfareRecord.setIntegCname(account.getIntegCname());
			ddbLevelupWelfareRecord.setLevelupWelfareRuleId(ddbLevelupWelfareRule.getZid());
			ddbLevelupWelfareRecord.setRealName(account.getRealName());
			ddbLevelupWelfareRecord.setRealCname(account.getRealCname());
			ddbLevelupWelfareRecord.setOrderId(flowId);
			ddbLevelupWelfareRecord.setRemark("福利赠送");
			Date date = new Date();
			ddbLevelupWelfareRecord.setCreateTime(date);
			ddbLevelupWelfareRecord.setUpdateTime(date);			
			ddbLevelupWelfareRecordMapper.insert(ddbLevelupWelfareRecord);			
		}
	}
}