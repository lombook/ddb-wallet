package com.jinglitong.springshop.vo;


import java.math.BigDecimal;
import java.util.Date;

public class ShopOrderRewardVo {

	/**
     * 主订单flowId
     */
    private String flowId;

    /**
     * 主订单订单号
     */
    private String orderSn;
      
    /**
     * 订单创建时间
     */
    private Date orderCreateTime;

    /**
     * 用户id
     */
    private String custId;

    /**
     * 销售激励额度S
     */
    private BigDecimal sValue;

    /**
     * 袋金币积分额度
     */
    private BigDecimal djbValue;


    /**
     * @return flow_id
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * @param flowId
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    /**
     * 获取订单号
     *
     * @return order_sn - 订单号
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * 设置订单号
     *
     * @param orderSn 订单号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }
    
    /**
     * 获取订单创建时间
     *
     * @return order_create_time - 订单创建时间
     */
    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    /**
     * 设置订单创建时间
     *
     * @param orderCreateTime 订单创建时间
     */
    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    /**
     * 获取用户id
     *
     * @return cust_id - 用户id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * 设置用户id
     *
     * @param custId 用户id
     */
    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }


    /**
     * 获取销售激励额度S
     *
     * @return s_value - 销售激励额度S
     */
    public BigDecimal getsValue() {
        return sValue;
    }

    /**
     * 设置销售激励额度S
     *
     * @param sValue 销售激励额度S
     */
    public void setsValue(BigDecimal sValue) {
        this.sValue = sValue;
    }

    /**
     * 获取袋金币积分额度
     *
     * @return djb_value - 袋金币积分额度
     */
    public BigDecimal getDjbValue() {
        return djbValue;
    }

    /**
     * 设置袋金币积分额度
     *
     * @param djbValue 袋金币积分额度
     */
    public void setDjbValue(BigDecimal djbValue) {
        this.djbValue = djbValue;
    }
}
