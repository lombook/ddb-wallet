package com.jinglitong.wallet.job.common;

public enum CertifacateTypeEnum {

	IDCARD("00","身份证"),PASSPORT("01","护照");
	private String code;//证码码
    private String name;//证件名称

    CertifacateTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
