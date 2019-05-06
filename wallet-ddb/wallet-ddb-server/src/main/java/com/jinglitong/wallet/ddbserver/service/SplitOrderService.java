package com.jinglitong.wallet.ddbserver.service;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.ddbserver.mapper.*;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SplitOrderService {

    @Autowired
    private DdbGoodsRuleMapper ddbGoodsRuleMapper;
    @Autowired
    private DdbOrderMapper ddbOrderMapper;
    @Autowired
    private DdbIntegralAccountMapper ddbIntegralAccountMapper;
    @Autowired
    private DdbSeplitOrderMapper ddbSeplitOrderMapper;
    @Autowired
    private DdbIntegralAccountService ddbIntegralAccountService;

    @Autowired
    private DdbShouldFrozenMapper ddbShouldFrozenMapper;


    @Autowired
    private RequireHistoryService requireHistoryService;
    
    @Autowired
    private DdbRuleRcMapper ddbRuleRcMapper;

    @Autowired
    private DdbIntegralWalletMapper ddbIntegralWalletMapper;

    @Autowired
    private DdbSkuSettleaccountMapper ddbSkuSettleaccountMapper;

    @Autowired
    private IntegerExchangeService integerExchangeService;

    /**
     * 处理订单
     * @param ddbOrder
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void splitOrder(DdbOrder ddbOrder) {
        //商品zid集合
        HashSet<String> orderZidList = new HashSet<>();
        DdbGoodsRule goodsRule = null;
        //匹配商品规格表
        List<DdbGoodsRule> ddbGoodsRules = ddbGoodsRuleMapper.selectAllGoodsRule();
        for (DdbGoodsRule ddbgoodRule:ddbGoodsRules) {
            orderZidList.add(ddbgoodRule.getZid());
            if(ddbgoodRule.getZid().equals(ddbOrder.getRuleId())){
                goodsRule = ddbgoodRule;
            }
        }
        boolean hasRuleId = orderZidList.contains(ddbOrder.getRuleId());
        if(!hasRuleId){
            log.info("订单不匹配orderId："+ddbOrder.getId());
            return;
        }else {
            //开始处理订单
            log.info("订单处理开始");
            if(goodsRule.getDdbGiveRuleList() == null && goodsRule.getDdbGrowRuleList() == null ){
                    if(goodsRule.getDdbGiveRuleList().size() == 0){
                        log.info("该商品没有相应赠送规则goodsRule："+goodsRule.getZid());
                    }
                   if(goodsRule.getDdbGrowRuleList().size() == 0 ){
                       log.info("该商品没有相应释放规则goodsRule："+goodsRule.getZid());
                   }
            }else {
                //拆单
                split(ddbOrder,goodsRule);
            }
        }

        log.info("订单处理成功结束orderId："+ddbOrder.getFlowId());
    }

    /**
     * 拆单
     * @param ddbOrder
     * @param goodsRule
     */
    @Transactional
    public void split(DdbOrder ddbOrder, DdbGoodsRule goodsRule) {
        if(goodsRule.getDdbGiveRuleList() != null && goodsRule.getDdbGiveRuleList().size() > 0){
            List<DdbGiveRule> ddbGiveRuleList = goodsRule.getDdbGiveRuleList();
            for (DdbGiveRule giveRule : ddbGiveRuleList) {
                //拆单入库+赠送转账
                splitAndTranfer(ddbOrder,giveRule);
            }
        }
        if(goodsRule.getDdbGrowRuleList() != null && goodsRule.getDdbGrowRuleList().size() > 0) {
            List<DdbGrowRule> ddbGrowRuleList = goodsRule.getDdbGrowRuleList();
            for (DdbGrowRule growRule : ddbGrowRuleList) {
                //拆单入库+释放
                splitAndFrozen(ddbOrder, growRule);
            }
        }
        //ddbOrderMapper.updateStateByFlowId(1,ddbOrder.getFlowId());
    }
    //拆单入库+释放
    @Transactional
    public void splitAndFrozen(DdbOrder ddbOrder, DdbGrowRule growRule ) {
        DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(growRule.getRealName());
        String dateTime = DateUtils.getDateTime();
        //生成拆单信息
        DdbSeplitOrder ddbSeplitOrder =new DdbSeplitOrder();
        //应返还的总金额
        Integer amount = growRule.getAmount() * Integer.parseInt(ddbOrder.getProductNum());
        ddbSeplitOrder.setAmount(amount);
        ddbSeplitOrder.setCreateTime(dateTime);
        ddbSeplitOrder.setCustId(ddbOrder.getUserId());
        ddbSeplitOrder.setFlowId(ddbOrder.getFlowId());
        ddbSeplitOrder.setRuleId(ddbOrder.getRuleId());
        ddbSeplitOrder.setGrule_id(growRule.getZid());
        ddbSeplitOrder.setOrderId(ddbOrder.getShopTrade());
        ddbSeplitOrder.setInteZid(ddbIntegralAccount.getZid());
        ddbSeplitOrder.setOrderId(ddbOrder.getShopTrade());
        ddbSeplitOrder.setRealName(growRule.getRealName());
        ddbSeplitOrder.setRealCname(growRule.getRealCname());
        ddbSeplitOrder.setZid(UuidUtil.getUUID());
        ddbSeplitOrder.setUpdateTime(dateTime);
        ddbSeplitOrder.setState(0);
        //拆单入库
        ddbSeplitOrderMapper.insert(ddbSeplitOrder);
        //入释放表
        DdbShouldFrozen ddbShouldFrozen = new DdbShouldFrozen();
        ddbShouldFrozen.setZid(UuidUtil.getUUID());
        ddbShouldFrozen.setAmount(amount);
        ddbShouldFrozen.setCreateTime(dateTime);
        ddbShouldFrozen.setCustId(ddbOrder.getUserId());
        ddbShouldFrozen.setExcuteTime(DateUtils.formatDateTime(DateUtils.addDays(new Date(),1)));
        ddbShouldFrozen.setFlowId(ddbOrder.getFlowId());
        ddbShouldFrozen.setFrozenDays(0);
        ddbShouldFrozen.setInteCname(ddbIntegralAccount.getIntegCname());
        ddbShouldFrozen.setInteName(ddbIntegralAccount.getIntegName());
        ddbShouldFrozen.setInteZid(ddbIntegralAccount.getZid());
        ddbShouldFrozen.setLeftAmount(amount);
        ddbShouldFrozen.setProportion(growRule.getProportion());
        ddbShouldFrozen.setRealName(ddbIntegralAccount.getRealName());
        ddbShouldFrozen.setRealCname(ddbIntegralAccount.getRealCname());
        BigDecimal amountOfday = new BigDecimal(amount).multiply(new BigDecimal(growRule.getProportion())).divide(new BigDecimal("100"));
        int roundDown = amountOfday.intValue();
        ddbShouldFrozen.setRfrozenAmount(roundDown);
        ddbShouldFrozen.setGrowRuleId(growRule.getZid());
        ddbShouldFrozen.setRuleId(ddbOrder.getRuleId());
        ddbShouldFrozen.setUpdateTime(dateTime);
        ddbShouldFrozenMapper.insert(ddbShouldFrozen);
        //修改拆过订单的状态
        ddbSeplitOrderMapper.updateStateByzId(1,ddbSeplitOrder.getZid());
    }

    /**
     * 拆单和转账
     * @param ddbOrder
     * @param giveRule
     */
    @Transactional
    public void splitAndTranfer(DdbOrder ddbOrder, DdbGiveRule giveRule) {
        DdbIntegralAccount ddbIntegralAccount = ddbIntegralAccountMapper.selectByRealName(giveRule.getRealName());
        String dateTime = DateUtils.getDateTime();
        //生成拆单信息
        DdbSeplitOrder ddbSeplitOrder =new DdbSeplitOrder();
        int amount = giveRule.getAmount() * Integer.parseInt(ddbOrder.getProductNum());
        ddbSeplitOrder.setAmount(amount);
        ddbSeplitOrder.setCreateTime(dateTime);
        ddbSeplitOrder.setCustId(ddbOrder.getUserId());
        ddbSeplitOrder.setFlowId(ddbOrder.getFlowId());
        ddbSeplitOrder.setRuleId(ddbOrder.getRuleId());
        ddbSeplitOrder.setZruleId(giveRule.getZid());
        ddbSeplitOrder.setOrderId(ddbOrder.getShopTrade());
        ddbSeplitOrder.setInteZid(ddbIntegralAccount.getZid());
        ddbSeplitOrder.setOrderId(ddbOrder.getShopTrade());
        ddbSeplitOrder.setRealName(giveRule.getRealName());
        ddbSeplitOrder.setRealCname(giveRule.getRealCname());
        ddbSeplitOrder.setZid(UuidUtil.getUUID());
        ddbSeplitOrder.setUpdateTime(dateTime);
        ddbSeplitOrder.setState(0);
        ddbSeplitOrderMapper.insert(ddbSeplitOrder);
        //转账
        ddbIntegralAccountService.addInteg(amount,ddbOrder.getUserId(),ddbOrder.getFlowId(),ddbIntegralAccount,"总账往积分转账");
        //修改状态
        ddbSeplitOrderMapper.updateStateByzId(1,ddbSeplitOrder.getZid());

    }


    /**
     * 插入订单
     */
    @Transactional
    public DdbOrder insertDdbOrder(Map<String, Object> map,String flowId) {
        //排重入库
        requireHistoryService.ddbRequireHistoryInsert(flowId, DateUtils.getDateTime());
        String ruleId = (String) map.get("ruleId");
        String orderCreateTime =(String) map.get("orderCreateTime");
        
        Weekend<DdbRuleRc>weekend = Weekend.of(DdbRuleRc.class);
    	WeekendCriteria<DdbRuleRc,Object> Criteria =weekend.weekendCriteria();
    	Criteria.andCondition(" is_special = 1 and rule_id ='"+ruleId+"' and effective_time <= '"+orderCreateTime+"' "
    			+ "and expiry_time >= '"+orderCreateTime+"' ");
    	DdbRuleRc ruleRc =  ddbRuleRcMapper.selectOneByExample(weekend);
        //订单插入
        DdbOrder ddbOrder = new DdbOrder();
        ddbOrder.setCreateTime(DateUtils.getDateTime());
        ddbOrder.setFlowId((String) map.get("flowId"));
        ddbOrder.setRuleId(ruleId);
        ddbOrder.setShopTrade((String) map.get("shopTrade"));
        ddbOrder.setProductNum((String) map.get("productNum"));
        ddbOrder.setPrice((String) map.get("price"));
        ddbOrder.setNonceStr( map.get("nonceStr").toString());
        ddbOrder.setUserId((String) map.get("userId"));
        ddbOrder.setState(false);
        ddbOrder.setKjState(false);
        ddbOrder.setSkuId((String) map.get("skuId"));
        ddbOrder.setOrderCreateTime(orderCreateTime);
        if(ruleRc != null) {//特殊商品。RC
        	Integer productNum = Integer.valueOf(ddbOrder.getProductNum());
        	Integer rcValue =  ruleRc.getRcValue() * productNum;
        	ddbOrder.setRcValue(rcValue);
        	ddbOrder.setSpecialNum(productNum);
        	
        	Integer gfValue = ruleRc.getGfValue() * productNum;
        	ddbOrder.setGfValue(gfValue);
    	}
        int insert = ddbOrderMapper.insert(ddbOrder);

        Weekend<DdbSkuSettleaccount>weekends = Weekend.of(DdbSkuSettleaccount.class);
    	WeekendCriteria<DdbSkuSettleaccount,Object> Criterias =weekends.weekendCriteria();
    	Criterias.andCondition(" rule_id ='"+ddbOrder.getRuleId()+"' and effective_time <= '"+orderCreateTime+"' and expiry_time >= '"+orderCreateTime+"'");
        DdbSkuSettleaccount ddbSkuSettleaccount = ddbSkuSettleaccountMapper.selectOneByExample(weekends);
        if(ddbSkuSettleaccount != null) {
        	Integer samount = Integer.valueOf(ddbOrder.getProductNum()) * ddbSkuSettleaccount.getAmount();
        	DdbIntegralAccount acc= ddbIntegralAccountMapper.selectByZid(ddbSkuSettleaccount.getInteId());
        	integerExchangeService.addInteg(samount, -samount, ddbSkuSettleaccount.getCustId(), ddbOrder.getFlowId(),
        			acc, "特殊商品向根节点转账", false);
        }
        //树的处理
        /*DdbSkuRuleTreeDic skuRuleTree = ddbSkuRuleTreeDicMapper.selectByRuleId((String) map.get("ruleId"));
        if(skuRuleTree != null){
            DdbIntegralAccount account = ddbIntegralAccountMapper.selectByZid(skuRuleTree.getInteId());
            //查询上级总账
            DdbIntegralAccount paccount = ddbIntegralAccountMapper.selectParentAByInte(skuRuleTree.getInteId());
            //验证用户是否有该钱包
            DdbIntegralWallet wallet = ddbIntegralWalletMapper.selectByRealNameAdnCustId(paccount.getRealName(), (String) map.get("userId"));
            if(wallet == null){
                //没有则创建
                wallet = new DdbIntegralWallet();
                wallet.setZid(UuidUtil.getUUID());
                wallet.setCustId((String) map.get("userId"));
                wallet.setInteName(paccount.getIntegName());
                wallet.setInteCname(paccount.getIntegCname());
                wallet.setAmount(0L);
                wallet.setCreateTime(DateUtils.getDateTime());
                wallet.setUpdateTime(DateUtils.getDateTime());
                ddbIntegralWalletMapper.insert(wallet);
            }
            //进行转账
            ddbIntegralAccountService.addInteg((int)map.get("productNum")*skuRuleTree.getTreeNum() * 100,(String) map.get("userId"),(String) map.get("flowId"),account,"买送");
        }*/
        return  ddbOrder;
    }
}
