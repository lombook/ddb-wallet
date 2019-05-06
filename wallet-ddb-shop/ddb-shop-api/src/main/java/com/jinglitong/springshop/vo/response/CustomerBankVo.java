package com.jinglitong.springshop.vo.response;

import java.util.Date;

public class CustomerBankVo {
    /**
     * 业务id
     */
    private String zid;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 用户展示图标
     */
    private String imgUrl;
    /**
     * 卡号
     */
    private String cardNo;


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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

}
