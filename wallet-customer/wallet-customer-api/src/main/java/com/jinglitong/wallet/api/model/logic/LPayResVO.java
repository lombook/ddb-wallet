package com.jinglitong.wallet.api.model.logic;

import java.util.HashMap;
import java.util.Map;

public class LPayResVO extends ErrorCode{
    private String chainId;
    private String txHash;
    private Map<String,Object> resMap = new HashMap<>();
    private String appId;

    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Map<String, Object> getResMap() {
        return resMap;
    }

    public void setResMap(Map<String, Object> resMap) {
        this.resMap = resMap;
    }
}
