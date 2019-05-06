package com.jinglitong.wallet.ddbapi.model.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 级差分红用户信息VO
 */
@Data
public class CustomerInfo4LevelDiff {

	/**
	 * 用户ID
	 */
	private String userid;
	/**
	 * 树节点层级
	 */
	private int treeNodeLevel;
	/**
	 * 上级用户列表
	 * 
	 * 第一个元素为跟用户，最后一个元素为直接上级。
	 * 即：若A邀请B，B邀请C，则A是B的上级，AB都是C的上级。C的parentsList中为{A,B}
	 */
	private List<String> parentsList;
	/** 
	 * 直接下级用户列表
	 */
	private List<String> directChildrenList;
	/** 
	 * 自己股东级别
	 */
	private int selfStockholderLevel = 0;
	/** 
	 * 自己个人消费总额（不包含自己育苗订单）
	 */
	private BigDecimal selfSumAmount = new BigDecimal(0);
	/** 
	 * 自己育苗消费总额
	 */
	private BigDecimal selfYumiaoSumAmount = new BigDecimal(0);
	/** 
	 * 团队消费总额（包含自己个人消费总额+自己育苗订单+直接下级每个人的团队）
	 */
	private BigDecimal teamSumAmount = new BigDecimal(0);
	/** 
	 * 团队成员中各个股级的人数
	 * 数据格式：Map<股东等级String, 人数Integer>
	 */
	private Map<String, Integer> teamMemberCntByStockholderLevel = new HashMap<String, Integer>();

	/**
	 * 自己购买的特殊商品数量
	 */
	private int ownSpecialNum=0;

	/**
	 * 直接下级购买的特殊商品数量
	 */
	private int dsubSpecialNum=0;

}