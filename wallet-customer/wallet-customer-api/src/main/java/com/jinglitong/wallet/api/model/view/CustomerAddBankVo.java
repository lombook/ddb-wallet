package com.jinglitong.wallet.api.model.view;

import java.util.Date;

public class CustomerAddBankVo extends BankDicVo{
   

	/**
     * 业务id
     */
    private String zid;
    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 开户行
     */
    private String activeAddress;
    
    /**
     * 持卡人
     */
    private String cardholder;
    
    

    public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getActiveAddress() {
		return activeAddress;
	}

	public void setActiveAddress(String activeAddress) {
		this.activeAddress = activeAddress;
	}

}