package com.jinglitong.wallet.give.service;

import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.give.mapper.*;
import com.jinglitong.wallet.give.util.DateUtils;
import com.jinglitong.wallet.give.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
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
    private DdbConsumerHistoryMapper ddbConsumerHistoryMapper;

    @Autowired
    private DdbGiveRuleMapper ddbGiveRuleMapper;

    @Autowired
    private DdbGrowRuleMapper ddbGrowRuleMapper;






    /**
     * 处理订单
     * @param ddbOrder
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void splitOrder(DdbOrder ddbOrder,com.aliyun.openservices.ons.api.Message message) {
        //商品zid集合
        HashSet<String> orderZidList = new HashSet<>();
        DdbGoodsRule goodsRule = new DdbGoodsRule();
        //匹配商品规格表
        List<DdbGoodsRule> ddbGoodsRules = ddbGoodsRuleMapper.selectAllGoodsRuleByZid(ddbOrder.getRuleId());
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
        DdbConsumerHistory ddbConsumerInsert = new DdbConsumerHistory();
        ddbConsumerInsert.setMsgId(message.getMsgID());
        ddbConsumerInsert.setCreateTime(DateUtils.getDateTime());
        ddbConsumerInsert.setFlowId(message.getKey());
        ddbConsumerHistoryMapper.insert(ddbConsumerInsert);
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
        log.info("time-----"+ddbOrder.getOrderCreateTime());
        String s = ddbOrder.getCreateTime();
        try {
             s = DateUtils.formatDateTime(DateUtils.addDays(DateUtils.parseDate(ddbOrder.getOrderCreateTime().substring(0, ddbOrder.getOrderCreateTime().length() - 2)), 1));
        }catch (Exception e){
            log.info("error"+e);
             s = ddbOrder.getOrderCreateTime();
            s= DateUtils.formatDateTime(DateUtils.addDays(DateUtils.parseDate(s), 1));
        }

        ddbShouldFrozen.setExcuteTime(s);
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
        log.info(ddbShouldFrozen.toString());
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
    public DdbOrder insertDdbOrder(Map<String, Object> map) {
        DdbOrder ddbOrder = new DdbOrder();
        ddbOrder.setCreateTime(DateUtils.getDateTime());
        ddbOrder.setFlowId((String) map.get("flowId"));
        ddbOrder.setRuleId((String) map.get("ruleId"));
        ddbOrder.setShopTrade((String) map.get("shopTrade"));
        ddbOrder.setProductNum((String) map.get("productNum"));
        ddbOrder.setPrice((String) map.get("price"));
        ddbOrder.setNonceStr((String) map.get("nonceStr"));
        ddbOrder.setUserId((String) map.get("userId"));
        ddbOrder.setState(false);
        ddbOrder.setKjState(false);
        ddbOrder.setSkuId((String) map.get("skuId"));
        ddbOrder.setOrderCreateTime((String) map.get("orderCreateTime"));
        int insert = ddbOrderMapper.insert(ddbOrder);
        return  ddbOrder;
    }
}
