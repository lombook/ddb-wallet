package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Table(name = "sku_price")
public class SkuPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * sku_id
     */
    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "currency_id")
    private String currencyId;

    private BigDecimal price;

    @Column(name = "price_code")
    private String priceCode;

    /**
     * 币种表数字币id
     */
    @Column(name = "integral_c_id")
    private String integralCId;

    @Column(name = "integral_price")
    private BigDecimal integralPrice;

    @Column(name = "integral_code")
    private String integralCode;

    /**
     * 0:纯法币 1:非纯法币
     */
    @Column(name = "price_type")
    private Integer priceType;

    /**
     * 是否默认(0否 1是)
     */
    @Column(name = "is_default")
    private Integer isDefault;
    
    @Column(name = "created_time")
    private Date createdTime;
    
    @Column(name = "updated_time")
    private Date updatedTime;

    
    
    public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
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
     * 获取sku_id
     *
     * @return sku_id - sku_id
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * 设置sku_id
     *
     * @param skuId sku_id
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    /**
     * @return currency_id
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId
     */
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId == null ? null : currencyId.trim();
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return price_code
     */
    public String getPriceCode() {
        return priceCode;
    }

    /**
     * @param priceCode
     */
    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode == null ? null : priceCode.trim();
    }

    /**
     * 获取币种表数字币id
     *
     * @return integral_c_id - 币种表数字币id
     */
    public String getIntegralCId() {
        return integralCId;
    }

    /**
     * 设置币种表数字币id
     *
     * @param integralCId 币种表数字币id
     */
    public void setIntegralCId(String integralCId) {
        this.integralCId = integralCId == null ? null : integralCId.trim();
    }

    /**
     * @return integral_price
     */
    public BigDecimal getIntegralPrice() {
        return integralPrice;
    }

    /**
     * @param integralPrice
     */
    public void setIntegralPrice(BigDecimal integralPrice) {
        this.integralPrice = integralPrice;
    }

    /**
     * @return integral_code
     */
    public String getIntegralCode() {
        return integralCode;
    }

    /**
     * @param integralCode
     */
    public void setIntegralCode(String integralCode) {
        this.integralCode = integralCode == null ? null : integralCode.trim();
    }

    /**
     * 获取0:纯法币 1:非纯法币
     *
     * @return price_type - 0:纯法币 1:非纯法币
     */
    public Integer getPriceType() {
        return priceType;
    }

    /**
     * 设置0:纯法币 1:非纯法币
     *
     * @param priceType 0:纯法币 1:非纯法币
     */
    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    /**
     * 获取是否默认(0否 1是)
     *
     * @return is_default - 是否默认(0否 1是)
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    /**
     * 设置是否默认(0否 1是)
     *
     * @param isDefault 是否默认(0否 1是)
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
}