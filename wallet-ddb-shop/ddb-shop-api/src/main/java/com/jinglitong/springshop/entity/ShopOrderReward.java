package com.jinglitong.springshop.entity;

import com.alibaba.fastjson.serializer.BigDecimalCodec;
import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;


@ApiModel(value = "订单激励表")
public class ShopOrderReward {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer  id;
   /**
    * 订单流水id
    */
   @Column(name = "flow_id")
   private String  flowId;
   /**
    * 订单号
    */
   @Column(name = "order_sn")
   private String  orderSn;
   /**
    * 用户id
    */
   @Column(name = "cust_id")
   private String  custId;
   /**
    * 激励处理状态（0未处理1已处理）
    */
   @Column(name = "reward_state")
   private Integer  rewardState;
   /**
    * 创建时间
    */
   @Column(name = "create_time")
   private Date createTime;
   /**
    * 订单创建时间
    */
   @Column(name = "order_create_time")
   private Date  orderCreateTime;
   /**
    * 订单创建时间
    */
   @Column(name = "update_time")
   private Date  updateTime;
   /**
    * 销售激励额度S
    */
   @Column(name = "s_value")
   private BigDecimal sValue;
   /**
    * 袋金币积分额度
    */
   @Column(name = "djb_value")
   private BigDecimal djbValue;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getFlowId() {
      return flowId;
   }

   public void setFlowId(String flowId) {
      this.flowId = flowId;
   }

   public String getOrderSn() {
      return orderSn;
   }

   public void setOrderSn(String orderSn) {
      this.orderSn = orderSn;
   }

   public String getCustId() {
      return custId;
   }

   public void setCustId(String custId) {
      this.custId = custId;
   }

   public Integer getRewardState() {
      return rewardState;
   }

   public void setRewardState(Integer rewardState) {
      this.rewardState = rewardState;
   }

   public Date getCreateTime() {
      return createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public Date getOrderCreateTime() {
      return orderCreateTime;
   }

   public void setOrderCreateTime(Date orderCreateTime) {
      this.orderCreateTime = orderCreateTime;
   }

   public BigDecimal getsValue() {
      return sValue;
   }

   public void setsValue(BigDecimal sValue) {
      this.sValue = sValue;
   }

   public BigDecimal getDjbValue() {
      return djbValue;
   }

   public void setDjbValue(BigDecimal djbValue) {
      this.djbValue = djbValue;
   }

   public Date getUpdateTime() {
      return updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }
}
