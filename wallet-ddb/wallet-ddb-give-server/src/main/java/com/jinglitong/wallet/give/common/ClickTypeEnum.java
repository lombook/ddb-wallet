package com.jinglitong.wallet.give.common;

public enum ClickTypeEnum {

	NOTICE("1", "公告"),
    ;

    private String code;//结果编码
    private String value;//结果信息

    ClickTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
