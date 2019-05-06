package com.jinglitong.springshop.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "订单邀请奖励规则表")
public class ShopRewardRule {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "zid")
  private String zid;
  /**
   * 奖励规则名称
   */
  @Column(name = "reward_name")
  private String rewardName;
  /**
   * 奖励规则类型
   * zjyq直接邀请,
   * jjyq间接邀请,
   * yjjd一级节点,
   * ddjl订单激励
   */
  @Column(name = "reward_type")
  private String rewardType;
  /**
   * 奖励比例
   */
  @Column(name = "reward_percent")
  private BigDecimal rewardPercent;
  /**
   * 总账积分id
   */
  @Column(name = "integral_id")
  private String integralId;
  /**
   * 创建时间
   */
  @Column(name = "create_time")
  private Date createTime;
  /**
   * 更新时间
   */
  @Column(name = "update_time")
  private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(BigDecimal rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public String getIntegralId() {
        return integralId;
    }

    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
