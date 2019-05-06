package com.jinglitong.wallet.api.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "customer_bank")
public class CustomerBank {
	 @Id
    private Integer id;
    /**
     * 业务id
     */
    private String zid;
    /**
     * 用户id
     */
    private String custId;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 图标
     */
    private String imgUrl;
    /**
     * 开户行
     */
    private String activeAddress;
    /**
     * 持卡人
     */
    private String cardholder;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 是否删除 0删除1可用
     */
    private Boolean flag;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

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

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getActiveAddress() {
        return activeAddress;
    }

    public void setActiveAddress(String activeAddress) {
        this.activeAddress = activeAddress;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}