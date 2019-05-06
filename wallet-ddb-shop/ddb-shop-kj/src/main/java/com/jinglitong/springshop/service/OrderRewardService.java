package com.jinglitong.springshop.service;

import com.jinglitong.springshop.OrderRewardContent;
import com.jinglitong.springshop.entity.*;
import com.jinglitong.springshop.mapper.*;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.ShopOrderRewardVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class OrderRewardService {

    @Autowired
    private ShopRewardRuleMapper shopRewardRuleMapper;
    @Autowired
    private ShopOrderRewardMapper shopOrderRewardMapper;
    @Autowired
    private ShopRewardSplitMapper shopRewardSplitMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private DdbIntegralAccountMapper integralAccountMapper;
    @Autowired
    private AccountTranfersService accountTranferService;

    /**
     * 订单奖励、直接邀请奖励、间接邀请、一级节点
     *
     * 转账并更新状态
     *
     * @param shopOrderRewardVo
     * @return
     */
    @Transactional
    public void SaleRewardAndTransfer(ShopOrderRewardVo shopOrderRewardVo) {

        //获取实体属性
        BigDecimal sValue = shopOrderRewardVo.getsValue();
        //销售激励额度S为null或0则返回
        if(!ifNull("sValue",sValue)){
            return;
        }
        String custId = shopOrderRewardVo.getCustId();
        String flowId = shopOrderRewardVo.getFlowId();
        String orderSn = shopOrderRewardVo.getOrderSn();
        Date orderCreateTime = shopOrderRewardVo.getOrderCreateTime();
        try {
            /**
             * 保存到shop_order_reward表中
             */
            ShopOrderReward shopOrderReward = new ShopOrderReward();
            saveOrderReward(shopOrderReward,custId, flowId, orderSn, orderCreateTime, sValue);
            shopOrderRewardMapper.saveShopOrderReward(shopOrderReward);

            Customer customer = new Customer();
            customer.setCustId(custId);
            //根据custId获取用户实体
            customer = customerMapper.getByCustId(customer);

            /** 获取邀请关系List
             * level  id     cust_id                              selfInvite    inviteCode
             * -----------------------------------------------------------------------------
             * 3	 1	    511f3aa1256243c4a9dbab84cb8f9a8e	A1000000
             * 2	6866	69215b047a2f48049b45f3a2e3ca2934	A1B10000	  A1000000
             * 1	6870	465f23f3df164d3bb68b76e940c349b3	A1B1C200	  A1B10000
             *
             * level为树节点倒序，值越大越靠近root，
             * inviteCode为空的为root节点，包含被查询用户自身
             *
             * 直接邀请：list的size要大于2，custId取倒数第二个
             * 间接邀请：list的size要大于3，custId取倒数第三个
             * 一级节点：list的size要大于，custId取正数第二个
             */
            ArrayList<HashMap<String, Object>> mapList = (ArrayList<HashMap<String, Object>>) customerMapper.getInviteList(customer);
            //判断是否为空
            if (mapList == null || mapList.size() <= 0) {
                log.error("大地宝邀请人关系为空，不执行");
            }
            DdbIntegralAccount integralAccountYq = null;
            DdbIntegralAccount integralAccountYqRes = null;
            ShopRewardRule shopRewardRuleYq = new ShopRewardRule();
            ShopRewardRule shopRewardRuleYqRes = new ShopRewardRule();
            //直接邀请奖励，用户custId取mapList.get(mapList.size() -2);
            if (mapList.size() <= 2) {
                log.error("大地宝直接邀请人为根节点或不存在，不发放激励，size: " + mapList.size());
            } else {
                HashMap<String, Object> mapDirect = mapList.get(mapList.size() -2);
                //查询shop_reward_rule表中的reward_type为zjyq的记录
                shopRewardRuleYq.setRewardType(OrderRewardContent.REWARD_ZJYQ);
                shopRewardRuleYqRes = shopRewardRuleMapper.getShopRewardRule(shopRewardRuleYq);
                //规则不为空才执行
                if(ifNotNull(shopRewardRuleYqRes)){
                    integralAccountYqRes = integralAccountMapper.selectByZid(shopRewardRuleYqRes.getIntegralId());
                    ShopRewardSplit shopRewardSplit11 = new ShopRewardSplit();
                    //组装ShopRewardSplit实体
                    saveRewardSplit(shopRewardSplit11, integralAccountYqRes, shopRewardRuleYqRes,
                            flowId, orderSn, sValue, mapDirect.get("cust_id").toString());
                    //保存到shop_reward_split表中
                    shopRewardSplitMapper.saveShopRewardSplit(shopRewardSplit11);
                    log.info("直接邀请奖励保存成功。");
                }
            }
            //间接邀请奖励，用户custId取mapList.get(mapList.size() -3);
            if (mapList.size() <= 3) {
                log.error("大地宝间接邀请人为根节点或不存在，不发放激励，size: " + mapList.size());
            } else {
                HashMap<String, Object> mapDirect = mapList.get(mapList.size() - 3);
                //查询shop_reward_rule表中的reward_type为zjyq的记录
                shopRewardRuleYq.setRewardType(OrderRewardContent.REWARD_JJYQ);
                shopRewardRuleYqRes = shopRewardRuleMapper.getShopRewardRule(shopRewardRuleYq);
                //规则不为空才执行
                if(ifNotNull(shopRewardRuleYqRes)){
                    integralAccountYqRes = integralAccountMapper.selectByZid(shopRewardRuleYqRes.getIntegralId());
                    ShopRewardSplit shopRewardSplit22 = new ShopRewardSplit();
                    //组装ShopRewardSplit实体
                    saveRewardSplit(shopRewardSplit22, integralAccountYqRes, shopRewardRuleYqRes,
                            flowId, orderSn, sValue, mapDirect.get("cust_id").toString());
                    //保存到shop_reward_split表中
                    shopRewardSplitMapper.saveShopRewardSplit(shopRewardSplit22);
                    log.info("间接邀请奖励保存成功。");
                }
            }

            //一级节点邀请奖励，用户custId取mapList.get(1);
            if (mapList.size() < 2) {
                log.error("大地宝一级节点邀请人为根节点或不存在，不发放激励，size: " + mapList.size());
            } else {
                HashMap<String, Object> mapDirect = mapList.get(1);
                //查询shop_reward_rule表中的reward_type为zjyq的记录
                shopRewardRuleYq.setRewardType(OrderRewardContent.REWARD_YJYQ);
                shopRewardRuleYqRes = shopRewardRuleMapper.getShopRewardRule(shopRewardRuleYq);
                //规则不为空才执行
                if(ifNotNull(shopRewardRuleYqRes)){
                    integralAccountYqRes = integralAccountMapper.selectByZid(shopRewardRuleYqRes.getIntegralId());
                    ShopRewardSplit shopRewardSplit33 = new ShopRewardSplit();
                    //组装到ShopRewardSplit实体
                    saveRewardSplit(shopRewardSplit33, integralAccountYqRes, shopRewardRuleYqRes,
                            flowId, orderSn, sValue, mapDirect.get("cust_id").toString());
                    //保存到shop_reward_split表中
                    shopRewardSplitMapper.saveShopRewardSplit(shopRewardSplit33);
                    log.info("一级邀请奖励保存成功。");
                }
            }
        }catch (Exception e){
            log.error("大地宝保存到订单奖励表或保存到订单奖励拆单失败，msg:",e);
            throw new RuntimeException();
        }

        ShopRewardSplit split = new ShopRewardSplit();
        split.setFlowId(flowId);
        List<ShopRewardSplit> splitsList = shopRewardSplitMapper.getShopRewardSplitList(split);
        for (ShopRewardSplit shopRewardSplit: splitsList) {
            Boolean flag = false;
            if (shopRewardSplit != null && shopRewardSplit.getAmount() > 0) {
                //执行转账
                log.info("执行转账，flowId："+shopRewardSplit.getFlowId()+"splitsList size : --------"+splitsList.size());
                try {
                    flag = accountTranferService.addInteg(shopRewardSplit.getAmount(), shopRewardSplit.getCustId(), shopRewardSplit.getIntegralId(),
                            shopRewardSplit.getFlowId(), OrderRewardContent.TRANSFER_TO_ONE, shopRewardSplit.getRealCname());
                } catch (Exception e) {
                    log.error("大地宝订单奖励转账失败，msg:" ,e);
                    throw new RuntimeException();
                }
                if (flag) {
                    //更新拆单shop_reward_split
                    shopRewardSplit.setStatus(OrderRewardContent.REWARD_ISSUED);
                    try {
                        shopRewardSplitMapper.updateShopRewardSplit(shopRewardSplit);
                    } catch (Exception e) {
                        log.error("大地宝更新订单激励拆单表失败：msg:",e);
                        throw new RuntimeException();
                    }
                }else {
                    log.error("大地宝转账失败，订单flowId:"+shopRewardSplit.getFlowId());
                    throw new RuntimeException();
                }
            }else {
                log.error("大地宝转账金额为空。");
            }
        }
        //更新激励表
        ShopOrderReward shopOrderRewardUpdate = new ShopOrderReward();
        shopOrderRewardUpdate.setFlowId(flowId);
        shopOrderRewardUpdate.setRewardState(OrderRewardContent.REWARD_ISSUED);
        try {
            shopOrderRewardMapper.updateRewardState(shopOrderRewardUpdate);
        } catch (Exception e) {
            log.error("大地宝更新订单激励表失败：msg:" ,e);
            throw new RuntimeException();
        }
    }
    public static void saveOrderReward (ShopOrderReward shopOrderReward, String custId, String flowId,
                                        String orderSn, Date orderCreateTime, BigDecimal sValue){
        shopOrderReward.setCustId(custId);
        shopOrderReward.setOrderSn(orderSn);
        shopOrderReward.setFlowId(flowId);
        shopOrderReward.setOrderCreateTime(orderCreateTime);
        shopOrderReward.setRewardState(OrderRewardContent.REWARD_UNISSUED);
        shopOrderReward.setsValue(sValue);
    }
    public static void saveRewardSplit(ShopRewardSplit shopRewardSplit, DdbIntegralAccount integralAccount,
                                       ShopRewardRule shopRewardRule, String flowId,
                                       String orderSn, BigDecimal rewardValue, String customerId) {
        shopRewardSplit.setCustId(customerId);
        shopRewardSplit.setFlowId(flowId);
        shopRewardSplit.setRewardPercent(shopRewardRule.getRewardPercent()
                .multiply(new BigDecimal(100)).intValue()+"%");
        shopRewardSplit.setRewardType(shopRewardRule.getRewardType());
        shopRewardSplit.setStatus(OrderRewardContent.REWARD_UNISSUED);
        shopRewardSplit.setRealCname(integralAccount.getRealCname());
        shopRewardSplit.setRealName(integralAccount.getRealName());
        shopRewardSplit.setIntegralId(integralAccount.getId().toString());
        shopRewardSplit.setZid(UuidUtil.getUUID());
        shopRewardSplit.setAmount(shopRewardRule.getRewardPercent().multiply(rewardValue)
                .multiply(new BigDecimal(100)).longValue());
        shopRewardSplit.setOrderSn(orderSn);
    }

    public boolean ifNotNull(ShopRewardRule shopRewardRule){
        if(shopRewardRule==null||shopRewardRule.getRewardType()==null
                ||shopRewardRule.getRewardType().equals("")
                ||shopRewardRule.getRewardPercent()==null
                ||shopRewardRule.getRewardPercent().equals("")){
            log.error("大地宝现贝邀请订单奖励规则信息为空");
            return false;
        }else {
            return true;
        }
    }

    /**
     * 判断sValue是否为空或0，为空或0则不继续执行
     * @param type
     * @param value
     */
    public boolean ifNull(String type, BigDecimal value){
        if(value==null||value.compareTo(new BigDecimal(0))==0){
            log.error(type+"值为："+value);
            return false;
        }else {
            return true;
        }
    }
}
