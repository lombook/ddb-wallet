package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;

/**
 * Created by fan on 2018/5/22.
 */
public class MainChainVO extends PageVO implements IAppIdModel {

    private Integer id;
    private String chainId;


    private String chainName;


    private String chainCurrency;


    private String chainInterface;


    private String chainCurrencyImg;


    private String chainDesc;


    private String handleName;

    private Boolean state;
    private Boolean activeFlag;

    private String appId;

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

    public MainChainVO() {
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainCurrency() {
        return chainCurrency;
    }

    public void setChainCurrency(String chainCurrency) {
        this.chainCurrency = chainCurrency;
    }

    public String getChainInterface() {
        return chainInterface;
    }

    public void setChainInterface(String chainInterface) {
        this.chainInterface = chainInterface;
    }

    public String getChainCurrencyImg() {
        return chainCurrencyImg;
    }

    public void setChainCurrencyImg(String chainCurrencyImg) {
        this.chainCurrencyImg = chainCurrencyImg;
    }

    public String getChainDesc() {
        return chainDesc;
    }

    public void setChainDesc(String chainDesc) {
        this.chainDesc = chainDesc;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }


    public Boolean getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
}
