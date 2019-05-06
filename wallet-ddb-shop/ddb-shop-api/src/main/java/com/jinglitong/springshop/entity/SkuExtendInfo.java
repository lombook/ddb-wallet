package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sku_extend_info")
public class SkuExtendInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 商品zid
     */
    @Column(name = "sku_id")
    private String skuId;
    
    @Column(name = "sku_price_id")
    private String skuPriceId;

    /**
     * 销售激励额度
     */
    @Column(name = "s_value")
    private BigDecimal sValue;

    /**
     * 袋金币积分
     */
    @Column(name = "djb_value")
    private BigDecimal djbValue;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    
    
    public String getSkuPriceId() {
		return skuPriceId;
	}

	public void setSkuPriceId(String skuPriceId) {
		this.skuPriceId = skuPriceId;
	}

	/**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * 获取商品zid
     *
     * @return sku_id - 商品zid
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * 设置商品zid
     *
     * @param skuId 商品zid
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    /**
     * 获取销售激励额度
     *
     * @return s_value - 销售激励额度
     */
    public BigDecimal getsValue() {
        return sValue;
    }

    /**
     * 设置销售激励额度
     *
     * @param sValue 销售激励额度
     */
    public void setsValue(BigDecimal sValue) {
        this.sValue = sValue;
    }

    /**
     * 获取袋金币积分
     *
     * @return djb_value - 袋金币积分
     */
    public BigDecimal getDjbValue() {
        return djbValue;
    }

    /**
     * 设置袋金币积分
     *
     * @param djbValue 袋金币积分
     */
    public void setDjbValue(BigDecimal djbValue) {
        this.djbValue = djbValue;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}