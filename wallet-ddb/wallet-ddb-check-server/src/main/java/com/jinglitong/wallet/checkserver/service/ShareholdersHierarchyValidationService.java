package com.jinglitong.wallet.checkserver.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralAccount;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.DdbRewardRule;
import com.jinglitong.wallet.ddbapi.model.DdbRewardRuleDetail;
import com.jinglitong.wallet.ddbapi.model.DdbShoreholderRule;
import com.jinglitong.wallet.ddbapi.model.logic.CustomerInfo4LevelDiff;
import com.jinglitong.wallet.ddbapi.model.logic.RewardRuleInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.checkserver.common.ConstantDict;
import com.jinglitong.wallet.checkserver.mapper.CustomerMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbGoodsRuleMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbIntegralAccountMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbRewardRuleDetailMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbRewardRuleMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbSeplitOrderRewardMapper;
import com.jinglitong.wallet.checkserver.mapper.DdbShoreholderRuleMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户层级结构校验服务
 * @author pan
 *
 */
//@Transactional
//@Service
@Slf4j
public class ShareholdersHierarchyValidationService {

	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	@Autowired
	private DdbRewardRuleMapper ddbRewardRuleMapper;
	@Autowired
	private DdbIntegralAccountMapper ddbIntegralAccountMapper;
	@Autowired
	private DdbRewardRuleDetailMapper ddbRewardRuleDetailMapper;
	@Autowired
	private DdbSeplitOrderRewardMapper ddbSeplitOrderRewardMapper;
	@Autowired
	private DdbShoreholderRuleMapper ddbShoreholderRuleMapper;
	@Autowired
	private CustomerMapper customerMapper;
	
	public void startCheck(DdbOrder order) {
		
		//1、获得产品价格(product_price)及分红规则(frule_group)。
		DdbGoodsRule goodsRuleInfo = getGoodsRuleInfoByRuleId(order.getRuleId());
		//2、计算获得订单总金额（单位分）
		//订单总金额(orderAmount) = 输入的商品数量（productNum）* 产品价格(product_price)
		BigDecimal orderAmount = new BigDecimal(order.getProductNum()).multiply(new BigDecimal(goodsRuleInfo.getProductPrice()));
		//3、组装分红规则Map
		Map<String, RewardRuleInfo> rewardRuleMap = makeRewardRuleMap(goodsRuleInfo.getFruleGroup());
		if(rewardRuleMap == null || rewardRuleMap.size() < 1) {
			return;
		}
		//4.根据订单中的用户id获取该用户信息。
		CustomerInfo4LevelDiff userHierarchyInfo = ConstantDict.VALIDATA_TREE_DATA.get(order.getUserId());
		//5. 取到订单，获取cust_id和parentIdList()
		List<String> parentIdList = userHierarchyInfo.getParentsList();
		//6. 取出参与一级和二级拓展分红的用户。计算其应分得的金额。
		if(parentIdList != null && parentIdList.size() > 0){
			
			//1.一级拓展分红金额校验
			for(int i = 0;i<parentIdList.size();i++) {
				//根据下标获取股东信息
				CustomerInfo4LevelDiff startShareholder = ConstantDict.VALIDATA_TREE_DATA.get(parentIdList.get(i));  
				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <= 0){
					continue;
				}
				RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("1");
				//分红基数比例(%)
				BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
				Map<String, String> fruleIdMap = ddbRewardRuleDetailMapper.selectZidByRuleType(1);
				//该股东分得的分红总金额
				BigDecimal divied_amount = orderAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
				//查询分红拆单表，通过flow_id、cust_id、frule_id、state,找到该订单的一级分红用户查询其分红金额SUM
				Integer total_sumAmount = ddbSeplitOrderRewardMapper.selectSumAmount(order.getFlowId(),parentIdList.get(i),fruleIdMap.get("zid"),1);
				//无误后，跳出循环
				if(divied_amount.subtract(new BigDecimal(total_sumAmount)).abs().compareTo(new BigDecimal(1)) <= 0) {
					break; 
				}
				//若数值不正确,发送邮件。
				if(divied_amount.subtract(new BigDecimal(total_sumAmount)).abs().compareTo(new BigDecimal(1)) > 0) {
					//通过cust_id查询account_id
					Customer customer = customerMapper.selectByCustId(parentIdList.get(i));
					ValidataService.CONTENT_MAP.put("一级拓展分红拆单金额校验", "一级拓展分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>分红规则zid: "+fruleIdMap.get("zid")+"<br>计算该订单一级拓展应分金额："+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
							+ "<br> 实际数据库中该订单一级拓展拆单总额："+(new BigDecimal(total_sumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br> 用户账号 : "+customer.getAccount()+"<br><br><br>");
					log.info("拆单金额校验","一级拓展分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>分红规则zid"+fruleIdMap.get("zid")+"<br>计算该订单一级拓展应分金额:"+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
							+ "<br> 实际数据库中该订单一级拓展拆单总额:"+(new BigDecimal(total_sumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br>"+"用户账号 :"+customer.getAccount());
					break;
				}
			}
			
			//2. 二级拓展分红金额校验
			int goon = 0;
			for(int i = 0;i<parentIdList.size();i++) {
				// 取完成直接分红用户之后的用户，进行二级分红
				CustomerInfo4LevelDiff startShareholder = ConstantDict.VALIDATA_TREE_DATA.get(parentIdList.get(i));  //获取该股东信息
				if(startShareholder == null || startShareholder.getSelfStockholderLevel() <= 0){
					continue;
				}
				if(startShareholder.getSelfStockholderLevel()>0 && goon==0){
					goon++;
					continue;
				}
				if(goon>=1 && startShareholder.getSelfStockholderLevel()>0){
					RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("2");
					BigDecimal rewardRate = rewardRuleInfo.getRewardRate().divide(new BigDecimal(100));
					Map<String, String> fruleIdMap = ddbRewardRuleDetailMapper.selectZidByRuleType(2);
					//该股东分得的总金额
					BigDecimal divied_amount = orderAmount.multiply(rewardRate).multiply(rewardRuleInfo.getRewardPercent()).divide(new BigDecimal(100));
					//查询分红拆单表，通过flow_id、cust_id、frule_id、state,找到该订单的一级分红用户查询其分红金额SUM
					Integer total_sumAmount = ddbSeplitOrderRewardMapper.selectSumAmount(order.getFlowId(),parentIdList.get(i),fruleIdMap.get("zid"),1);
					//无误后，跳出循环
					if(divied_amount.subtract(new BigDecimal(total_sumAmount)).abs().compareTo(new BigDecimal(1)) <= 0) {
						break;
					}
					//若数值不正确，发送邮件。
					if(divied_amount.subtract(new BigDecimal(total_sumAmount)).abs().compareTo(new BigDecimal(1)) > 0) {
						//通过cust_id查询account_id
						Customer customer = customerMapper.selectByCustId(parentIdList.get(i));
						ValidataService.CONTENT_MAP.put("二级拓展分红拆单金额校验", "二级拓展分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>分红规则zid: "+fruleIdMap.get("zid")+"<br>计算该订单二级拓展应分金额: "+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
								+ "<br> 实际数据库中该订单二级拓展拆单总额: "+(new BigDecimal(total_sumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br>用户账号 : "+customer.getAccount()+"<br><br><br>");
						log.info("拆单金额校验","二级拓展分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>分红规则zid: "+fruleIdMap.get("zid")+"<br>计算该订单二级拓展应分金额: "+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
								+ "<br> 实际数据库中该订单二级拓展拆单总额: "+(new BigDecimal(total_sumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br>用户账号 : "+customer.getAccount());
						break;
					}
				}
			}
			
			//3.级差分红金额校验： 取订单用户的上级中级别最高的用户的级别。查询该级别对应的分红比例。然后对比数据库中该订单的所有级差分红拆单总额SUM是否与计算的一致。
			Map<String, DdbShoreholderRule> ddbShoreholderRule = getShoreholderRuleMap();
			
			int maxLevel = 0;
			for(int i = 0;i<parentIdList.size();i++) {
				Integer level = ConstantDict.VALIDATA_TREE_DATA.get(parentIdList.get(i)).getSelfStockholderLevel();
				if(level > maxLevel) {
					maxLevel = level;
				}
			}
			if(maxLevel > 0){
				//根据maxLevel和rule_type查询数据库的到该级别的分红比例。
				int dividedRate = ddbRewardRuleDetailMapper.selectRateBylevelAndType(ddbShoreholderRule.get(""+maxLevel).getLevelDefine(),3);
				//计算总得分红金额
				BigDecimal totalDividedAmount = orderAmount.multiply(new BigDecimal(dividedRate)).divide(new BigDecimal(100));
				
				//查询该订单的所有级差分红的金额总和。
				Map<String,String> userToBonus = new HashMap<>();  //可以进行级差分红的用户id
				Map<String,List<String>> userToBonus2 = new HashMap<>();  //可以进行加权分红的用户id
				if(parentIdList != null && parentIdList.size() > 0){
					//判断每个股东的等级
					for(int i = 0;i < parentIdList.size(); i++){
						boolean isMax = true;
						CustomerInfo4LevelDiff userInfo = ConstantDict.VALIDATA_TREE_DATA.get(parentIdList.get(i));
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
									userToBonus.put(""+userInfo.getSelfStockholderLevel(), parentIdList.get(i));
									continue;
								}else if(!isMax && userInfo.getSelfStockholderLevel() > 1){
									if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
										List<String> userList = new ArrayList<>();
										userList.add(parentIdList.get(i));
										userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
										continue;
									}else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
										userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(parentIdList.get(i));
										continue;
									}
								}
							}
							userToBonus.put(""+userInfo.getSelfStockholderLevel(), parentIdList.get(i));
						}else if(userInfo.getSelfStockholderLevel() > 1 && userToBonus.get(""+userInfo.getSelfStockholderLevel()) != null){
							if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) == null){
								List<String> userList = new ArrayList<>();
								userList.add(parentIdList.get(i));
								userToBonus2.put(""+userInfo.getSelfStockholderLevel(), userList);
							}else if(userToBonus2.get(""+userInfo.getSelfStockholderLevel()) != null){
								userToBonus2.get(""+userInfo.getSelfStockholderLevel()).add(parentIdList.get(i));
							}
						}
					}
				}
				
				//查询参与级差分红的股东层级数
				List<String> levelDefineList = ddbRewardRuleDetailMapper.selectLevelNum(3);
				//判断参与级差分红的map数据阶段，匹配相应的分红规则
				StringBuilder bonusSystem = new StringBuilder();
				for(int a = 0;a < levelDefineList.size();a++){
					int levelNo = ddbShoreholderRule.get(levelDefineList.get(a)).getLevelSeqNo();
					if(userToBonus.get(levelNo+"") != null){
						bonusSystem.append("1");
					}else{
						bonusSystem.append("0");
					};
				}
				int totalSumAmount = 0;
				//极差分红开始=============
				if(ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()) != null){
					//循环参与级差分红的用户信息
					for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
						if(ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()).get(userMap.getKey()) != null) {
							String bnousInfo = ConstantDict.INIT_BONUS_SYSTEM.get(bonusSystem.toString()).get(userMap.getKey());
							String[] strs = bnousInfo.split("_");
							String fruleId = strs[0];   //该级股东级别在分红规则明细表中的zid
							
							//根据frule_id和flow_id和custmer_id以及state从分红拆单表中判断该用户是否存在加权分红
							int count = customerMapper.selectOrderCount(order.getFlowId(), userMap.getValue(), fruleId, 1);
							if(count <= 0 ) {
								Customer customer = customerMapper.selectByCustId(userMap.getValue());
								ValidataService.CONTENT_MAP.put("级差分红校验", "级差分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+""
										+"<br>原因可能为用户账户为"+customer.getAccount()+"的用户未得到分红"+"<br><br><br>");
								log.info("级差分红校验", "级差分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+""
										+"原因可能为用户账户为"+customer.getAccount()+"的用户未得到分红");
							}
							
							//通过flow_id和fruleId从数据库中确定用户分红总和
							Integer personSumAmount = ddbSeplitOrderRewardMapper.selectSumAmount(order.getFlowId(), userMap.getValue().toString(), fruleId, 1);
							if(null == personSumAmount) {
								personSumAmount = 0;
							}
							totalSumAmount += personSumAmount;
						}
					}
				}
				
				//数据库中的金额同计算不一致，发送邮件
				if(totalDividedAmount.subtract(new BigDecimal(totalSumAmount)).abs().compareTo(new BigDecimal(userToBonus.size()*3)) > 0){
					StringBuilder accountList = new StringBuilder();
					for (Map.Entry<String, String> userMap : userToBonus.entrySet()) {
						//循环参与级差分红的用户id，找到其账号
						String accountid = customerMapper.selectByCustId(userMap.getValue()).getAccount();
						accountList.append(accountid+"; ");
					}
					ValidataService.CONTENT_MAP.put("级差分红拆单金额校验", "级差分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>计算该订单级差分红应分金额: "+totalDividedAmount.divide(new BigDecimal(100)).setScale(2).toString()+""
							+ "<br> 实际数据库中该订单极差分红拆单总额: "+(new BigDecimal(totalSumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br> 涉及账号: "+accountList+"<br><br><br>");
					log.info("拆单金额校验","级差分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>计算该订单级差分红应分金额: "+totalDividedAmount.divide(new BigDecimal(100)).setScale(2).toString()+""
							+ "<br> 实际数据库中该订单极差分红拆单总额: "+(new BigDecimal(totalSumAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br> 涉及账号: "+accountList);
				}
				
				if(maxLevel > 1){
					//8. 找到参与加权分红的用户，根据股东级别分别计算比较数据库中的金额SUM是否同
					//查询参与加权分红的股东层级数
					List<String> levelDefineList2 = ddbRewardRuleDetailMapper.selectLevelNum(4);
					//获取加权分红的分红规则信息
					RewardRuleInfo rewardRuleInfo = rewardRuleMap.get("4");
					//获取包含六个不同股东层级分红比例信息的map
					Map<String, String> ruleDetailMap = rewardRuleInfo.getRuleDetailMap();
					//处理用户信息
					for(int b = 0;b < levelDefineList2.size();b++){
						String levelDefine = ddbShoreholderRule.get(levelDefineList2.get(b)).getLevelDefine();
						int levelNo = ddbShoreholderRule.get(levelDefineList2.get(b)).getLevelSeqNo();
						int diffrence = 0;
						if(userToBonus2.get(""+levelNo) != null && ruleDetailMap.get(levelDefine) != null){
							diffrence += userToBonus2.get(""+levelNo).size();
							String value = ruleDetailMap.get(levelDefine);
							String[] strs = value.split(":");
							String fruleId = strs[0];  //分红规则明细Zid
							String rate = strs[1]; //分红比例
							//该级别股东分得的总金额
							BigDecimal divied_amount = orderAmount.multiply(new BigDecimal(rate)).divide(new BigDecimal(100));
							//数据库中该级别所有用户的分红总额
							Integer levelTotalAmount = 0;
							for(String custmer_id:userToBonus2.get(""+levelNo)){
								//根据frule_id和flow_id和custmer_id以及state从分红拆单表中判断该用户是否存在加权分红
								int count = customerMapper.selectOrderCount(order.getFlowId(), custmer_id, fruleId, 1);
								if(count <= 0 ) {
									ValidataService.CONTENT_MAP.put("同星分红校验", "同星分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+""
											+"<br> 原因可能为用户账户为"+customerMapper.selectByCustId(custmer_id).getAccount()+"的用户未得到分红"+"<br><br><br>");
									log.info("拆单金额校验","同星分红校验", "同星分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+""
											+"<br> 原因可能为用户账户为"+customerMapper.selectByCustId(custmer_id).getAccount()+"的用户未得到分红");
									break;
								}
								//通过flow_id和fruleId从数据库中确定用户分红总和
								Integer personSumAmount = ddbSeplitOrderRewardMapper.selectSumAmount(order.getFlowId(),custmer_id, fruleId, 1);
								levelTotalAmount += personSumAmount;
							}
							//若不相同，发送告警邮件
							if(divied_amount.subtract(new BigDecimal(levelTotalAmount)).abs().compareTo(new BigDecimal(diffrence*3+1)) > 0) {
								//涉及账户
								StringBuilder accountid = new StringBuilder();
								for(String custmer_id:userToBonus2.get(""+levelNo)){
									String acount_id = customerMapper.selectByCustId(custmer_id).getAccount();
									accountid.append(acount_id+"; "); 
								}
								ValidataService.CONTENT_MAP.put("同星分红拆单金额校验", "同星分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>计算该订单同星分红应分金额: "+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
										+ "<br> 实际数据库中该订单同星分红拆单总额: "+(new BigDecimal(levelTotalAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br> 涉及用户账户 : "+accountid+"<br><br><br>");
								log.info("拆单金额校验","同星分红拆单金额校验不一致，请及时查看处理！订单号: "+order.getShopTrade()+"<br>计算该订单同星分红应分金额："+divied_amount.divide(new BigDecimal(100)).setScale(2).toString()+""
										+ "<br> 实际数据库中该订单同星分红拆单总额："+(new BigDecimal(levelTotalAmount)).divide(new BigDecimal(100)).setScale(2).toString()+"<br>"+"涉及用户账户 ："+accountid);
							}
						}
					}
				}
			}
		}
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
	
}
