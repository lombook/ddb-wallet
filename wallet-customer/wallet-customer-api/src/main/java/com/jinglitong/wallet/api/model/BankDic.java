package com.jinglitong.wallet.api.model;

import javax.persistence.Table;

@Table(name = "bank_dic")
public class BankDic {
    private Integer id;

    /**
     * 业务id
     */
    private String zid;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 选择银行图标
     */
    private String selUrl;
    /**
     * 用户展示图标
     */
    private String showUrl;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public String getSelUrl() {
		return selUrl;
	}

	public void setSelUrl(String selUrl) {
		this.selUrl = selUrl;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
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