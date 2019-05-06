package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
@Table(name = "frozen_release_rule")
public class FrozenReleaseRule implements IAppIdModel{
    /**
     * 规则id
     */
    @Id
    private Integer id;
    
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 释放类型 0:匀速 1:定期释放
     */
    @Column(name = "fr_type")
    private Integer frType;

    /**
     * 链ID
     */
    @Column(name = "chian_id")
    private String chianId;

    /**
     * 币ID
     */
    @Column(name = "coin_id")
    private String coinId;

    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    /**
     * 冻结状态 0:保存未启动,1:启动中,2:规则整个完成
     */
    @Column(name = "fr_status")
    private Integer frStatus;

    /**
     * 备注
     */
    private String mom;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 私钥
     */
    @Column(name = "pay_passwd")
    private String payPasswd;

    /**
     * 钱包app
     */
    @Column(name = "app_id")
    private String appId;
    
    @Transient
    private String walletName;

    @Transient
    private SubChain subChain;

    @Transient
    private MainChain mainChain;

    @Transient
    private List<FrozenReleaseDetailRule> details;

    @Transient
    private List<FrozenReleaseExcelSource> sourceList;

    public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
    /**
     * 获取规则id
     *
     * @return rule_id - 规则id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * 设置规则id
     *
     * @param ruleId 规则id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * 获取释放类型 0:匀速 1:定期释放
     *
     * @return fr_type - 释放类型 0:匀速 1:定期释放
     */
    public Integer getFrType() {
        return frType;
    }

    /**
     * 设置释放类型 0:匀速 1:定期释放
     *
     * @param frType 释放类型 0:匀速 1:定期释放
     */
    public void setFrType(Integer frType) {
        this.frType = frType;
    }

    /**
     * 获取链ID
     *
     * @return chian_id - 链ID
     */
    public String getChianId() {
        return chianId;
    }

    /**
     * 设置链ID
     *
     * @param chianId 链ID
     */
    public void setChianId(String chianId) {
        this.chianId = chianId;
    }

    /**
     * 获取币ID
     *
     * @return coin_id - 币ID
     */
    public String getCoinId() {
        return coinId;
    }

    /**
     * 设置币ID
     *
     * @param coinId 币ID
     */
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    /**
     * 获取公钥
     *
     * @return public_key - 公钥
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置公钥
     *
     * @param publicKey 公钥
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取冻结状态 0:保存未启动,1:启动中,2:规则整个完成
     *
     * @return fr_status - 冻结状态 0:保存未启动,1:启动中,2:规则整个完成
     */
    public Integer getFrStatus() {
        return frStatus;
    }

    /**
     * 设置冻结状态 0:保存未启动,1:启动中,2:规则整个完成
     *
     * @param frStatus 冻结状态 0:保存未启动,1:启动中,2:规则整个完成
     */
    public void setFrStatus(Integer frStatus) {
        this.frStatus = frStatus;
    }

    /**
     * 获取备注
     *
     * @return mom - 备注
     */
    public String getMom() {
        return mom;
    }

    /**
     * 设置备注
     *
     * @param mom 备注
     */
    public void setMom(String mom) {
        this.mom = mom;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取私钥
     *
     * @return pay_passwd - 私钥
     */
    public String getPayPasswd() {
        return payPasswd;
    }

    /**
     * 设置私钥
     *
     * @param payPasswd 私钥
     */
    public void setPayPasswd(String payPasswd) {
        this.payPasswd = payPasswd;
    }


    public List<FrozenReleaseDetailRule> getDetails() {
        return details;
    }

    public void setDetails(List<FrozenReleaseDetailRule> details) {
        this.details = details;
    }
    
    /**
     * 冻结标题
     */
    @Column(name = "rule_name")
    private String ruleName;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
}