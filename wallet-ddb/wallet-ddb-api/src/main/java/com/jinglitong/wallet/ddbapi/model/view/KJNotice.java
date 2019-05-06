package com.jinglitong.wallet.ddbapi.model.view;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class KJNotice {
    /**
     * 请求流水
     */
    private String flowId;
    /**
     * 订单主键（废弃）
     */
    private Integer id;
    /**
     * 订单号
     */
    private String shopTrade;
    /**
     * 商品数量
     */
    private String productNum;
    /**
     * 用户id
     */
    private String custId;
    /**
     * 规则id
     */
    private String ruleId;
    /**
     * 育苗购买记录金额/计算得到的订单金额
     */
    private BigDecimal amount;
    /**
     * 类型 1,订单 2,育苗购买 3,新增注册 
     */
    private String type;
    /**
     * 订单RC合计值
     */
    private int rcValue;
    
    private int gfValue;
    
    /**
     * 订单中特定商品的合计数量
     */
    private int specialNum;

}
