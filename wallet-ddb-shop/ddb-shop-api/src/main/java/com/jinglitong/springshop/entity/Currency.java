package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;
@Data
@ToString
public class Currency {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;
    /**
     * 货币代码
     */
    @Column(name = "currencyCode")
    private String currencycode;

    /**
     * 货币名称
     */
    @Column(name = "currencyName")
    private String currencyname;

    /**
     * 数字代码
     */
    @Column(name = "digitalCode")
    private String digitalcode;

    /**
     * 货币符号
     */
    @Column(name = "currencySign")
    private String currencysign;

    /**
     * 货币单位
     */
    @Column(name = "currencyUnit")
    private String currencyunit;

    /**
     * 是否数字货币 0 法币 1 代币
     */
    @Column(name = "isDigitalCurrency")
    private Boolean isdigitalcurrency;

    /**
     * 链类型(0:井通链　1:MAE 2:以太坊)
     */
    @Column(name = "chainType")
    private Byte chaintype;

    /**
     * 是否启用(0:不启用 1:启用)
     */
    @Column(name = "isEnabled")
    private Byte isenabled;

    /**
     * 手机端图标地址测试
     */
    @Column(name = "iconSrc")
    private String iconsrc;

    /**
     * 排序
     */
    private Integer orders;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    
    /**
     * 获取货币代码
     *
     * @return currencyCode - 货币代码
     */
    public String getCurrencycode() {
        return currencycode;
    }

    /**
     * 设置货币代码
     *
     * @param currencycode 货币代码
     */
    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode == null ? null : currencycode.trim();
    }

    /**
     * 获取货币名称
     *
     * @return currencyName - 货币名称
     */
    public String getCurrencyname() {
        return currencyname;
    }

    /**
     * 设置货币名称
     *
     * @param currencyname 货币名称
     */
    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname == null ? null : currencyname.trim();
    }

    /**
     * 获取数字代码
     *
     * @return digitalCode - 数字代码
     */
    public String getDigitalcode() {
        return digitalcode;
    }

    /**
     * 设置数字代码
     *
     * @param digitalcode 数字代码
     */
    public void setDigitalcode(String digitalcode) {
        this.digitalcode = digitalcode == null ? null : digitalcode.trim();
    }

    /**
     * 获取货币符号
     *
     * @return currencySign - 货币符号
     */
    public String getCurrencysign() {
        return currencysign;
    }

    /**
     * 设置货币符号
     *
     * @param currencysign 货币符号
     */
    public void setCurrencysign(String currencysign) {
        this.currencysign = currencysign == null ? null : currencysign.trim();
    }

    /**
     * 获取货币单位
     *
     * @return currencyUnit - 货币单位
     */
    public String getCurrencyunit() {
        return currencyunit;
    }

    /**
     * 设置货币单位
     *
     * @param currencyunit 货币单位
     */
    public void setCurrencyunit(String currencyunit) {
        this.currencyunit = currencyunit == null ? null : currencyunit.trim();
    }

    /**
     * 获取是否数字货币 0 法币 1 代币
     *
     * @return isDigitalCurrency - 是否数字货币 0 法币 1 代币
     */
    public Boolean getIsdigitalcurrency() {
        return isdigitalcurrency;
    }

    /**
     * 设置是否数字货币 0 法币 1 代币
     *
     * @param isdigitalcurrency 是否数字货币 0 法币 1 代币
     */
    public void setIsdigitalcurrency(Boolean isdigitalcurrency) {
        this.isdigitalcurrency = isdigitalcurrency;
    }

    /**
     * 获取链类型(0:井通链　1:MAE 2:以太坊)
     *
     * @return chainType - 链类型(0:井通链　1:MAE 2:以太坊)
     */
    public Byte getChaintype() {
        return chaintype;
    }

    /**
     * 设置链类型(0:井通链　1:MAE 2:以太坊)
     *
     * @param chaintype 链类型(0:井通链　1:MAE 2:以太坊)
     */
    public void setChaintype(Byte chaintype) {
        this.chaintype = chaintype;
    }

    /**
     * 获取是否启用(0:不启用 1:启用)
     *
     * @return isEnabled - 是否启用(0:不启用 1:启用)
     */
    public Byte getIsenabled() {
        return isenabled;
    }

    /**
     * 设置是否启用(0:不启用 1:启用)
     *
     * @param isenabled 是否启用(0:不启用 1:启用)
     */
    public void setIsenabled(Byte isenabled) {
        this.isenabled = isenabled;
    }

    /**
     * 获取手机端图标地址测试
     *
     * @return iconSrc - 手机端图标地址测试
     */
    public String getIconsrc() {
        return iconsrc;
    }

    /**
     * 设置手机端图标地址测试
     *
     * @param iconsrc 手机端图标地址测试
     */
    public void setIconsrc(String iconsrc) {
        this.iconsrc = iconsrc == null ? null : iconsrc.trim();
    }

    /**
     * 获取排序
     *
     * @return orders - 排序
     */
    public Integer getOrders() {
        return orders;
    }

    /**
     * 设置排序
     *
     * @param orders 排序
     */
    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后更新时间
     *
     * @return update_time - 最后更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后更新时间
     *
     * @param updateTime 最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}