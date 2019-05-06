package com.jinglitong.wallet.checkserver.mapper;


import com.jinglitong.wallet.ddbapi.model.DdbGoodsRule;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.checkserver.util.MyMapper;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface DdbGoodsRuleMapper extends MyMapper<DdbGoodsRule> {
    /**
     * 根据商品id获取商品详情
     * @param ruleId
     * @return
     */
	DdbGoodsRule selectByZid(String ruleId);

	/**
	 * 查询所有的商品详情
	 * @return
	 */
    List<DdbGoodsRule> selectAllGoodsRule();
    
    /**
     * 通过商品id查询在生效期内的商品详情
     * @param ruleId
     * @return
     */
    DdbGoodsRule selectByZidAndTime(String ruleId);
    
    /**
     * 根据订单信息获取商品信息
     * @param ddbOrder
     * @return
     */
    DdbGoodsRule selectDdbGoodsRuleDetail(KJNotice order);
    
}