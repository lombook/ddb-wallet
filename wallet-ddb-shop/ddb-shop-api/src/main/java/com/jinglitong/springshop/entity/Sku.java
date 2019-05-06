package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
@Data
@ToString
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 是否默认
     */
    @Column(name = "isDefault")
    private Boolean isdefault;

    /**
     * 市场价
     */
    @Column(name = "marketPrice")
    private BigDecimal marketprice;
    
    private String currencyId;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 商品编码
     */
    private String sn;

    /**
     * 是否售罄
     */
    @Column(name = "sell_out")
    private Integer sellOut;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private String productId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;


    @Column(name = "currencyItems")
    private String currencyItems;
    /**
     * 规格
     */
    @Column(name = "specificationValues")
    private String specificationvalues;
    
    @Column(name = "integral_price")
    private String integralPrice;
    
    @Column(name = "integral_c_id")
    private String integralCId;
    
    @Column(name = "price_code")
    private String priceCode;
    
    @Column(name = "integral_code")
    private String integralCode;
    
    @Column(name = "support_integral")
    private Integer supportIntegral;
    
    @Column(name = "update_oper")
    private String updateOper;
    
    
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
     * 获取成本价
     *
     * @return cost - 成本价
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本价
     *
     * @param cost 成本价
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取是否默认
     *
     * @return isDefault - 是否默认
     */
    public Boolean getIsdefault() {
        return isdefault;
    }

    /**
     * 设置是否默认
     *
     * @param isdefault 是否默认
     */
    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    /**
     * 获取市场价
     *
     * @return marketPrice - 市场价
     */
    public BigDecimal getMarketprice() {
        return marketprice;
    }

    /**
     * 设置市场价
     *
     * @param marketprice 市场价
     */
    public void setMarketprice(BigDecimal marketprice) {
        this.marketprice = marketprice;
    }

    /**
     * 获取售价
     *
     * @return price - 售价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置售价
     *
     * @param price 售价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取商品编码
     *
     * @return sn - 商品编码
     */
    public String getSn() {
        return sn;
    }

    /**
     * 设置商品编码
     *
     * @param sn 商品编码
     */
    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    /**
     * 获取是否售罄
     *
     * @return sell_out - 是否售罄
     */
    public Integer getSellOut() {
        return sellOut;
    }

    /**
     * 设置是否售罄
     *
     * @param sellOut 是否售罄
     */
    public void setSellOut(Integer sellOut) {
        this.sellOut = sellOut;
    }

    /**
     * 获取产品id
     *
     * @return product_id - 产品id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 设置产品id
     *
     * @param productId 产品id
     */
    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取规格
     *
     * @return specificationValues - 规格
     */
    public String getSpecificationvalues() {
        return specificationvalues;
    }

    /**
     * 设置规格
     *
     * @param specificationvalues 规格
     */
    public void setSpecificationvalues(String specificationvalues) {
        this.specificationvalues = specificationvalues == null ? null : specificationvalues.trim();
    }
}