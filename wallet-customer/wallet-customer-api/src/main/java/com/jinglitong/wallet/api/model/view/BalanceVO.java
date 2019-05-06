package com.jinglitong.wallet.api.model.view;

import java.util.ArrayList;
import java.util.List;

import com.jinglitong.wallet.api.model.IAppIdModel;

public class BalanceVO implements IAppIdModel{

    private String address;
    private String chainId;
    private String custId;
    private String pageNum;
    private String marker;
    private String appId;
    private List<String> tokens = new ArrayList<>();

    
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }


    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }


    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }


    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
