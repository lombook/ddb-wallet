package com.jinglitong.wallet.api.model.walletVo;

import com.jinglitong.wallet.api.model.IAppIdModel;
import com.jinglitong.wallet.api.model.view.PageVO;

import java.util.ArrayList;
import java.util.List;

public class FrozenReleaseVO extends PageVO implements IAppIdModel{
    private String ruleId;
    private String frType;
    private String chainId;
    private String coinId;
    private String publicKey;
    private String secret;
    private Integer frStatus;
    private String mom;
    private String createTime;
    private String updateTime;
    private String appId;
    private String ruleName;
    private String account;
    private Integer id;
    private List<FRDatilRuleVO> dataList = new ArrayList<>();

    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getFrType() {
        return frType;
    }

    public void setFrType(String frType) {
        this.frType = frType;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getFrStatus() {
        return frStatus;
    }

    public void setFrStatus(Integer frStatus) {
        this.frStatus = frStatus;
    }

    public String getMom() {
        return mom;
    }

    public void setMom(String mom) {
        this.mom = mom;
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


    public List<FRDatilRuleVO> getDataList() {
        return dataList;
    }

    public void setDataList(List<FRDatilRuleVO> dataList) {
        this.dataList = dataList;
    }

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
