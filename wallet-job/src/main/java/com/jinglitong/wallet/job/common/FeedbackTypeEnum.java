package com.jinglitong.wallet.job.common;

public enum FeedbackTypeEnum  {
	OPION_FEEDBACK("1","问题反馈"),PRODUCT_SUGGEST("2","产品建议"),
	FEATURE_APPLY("3","功能申请"),OTHER("4","其他");
	private String code;//反馈类型编码
    private String name;//反馈类型名称
    FeedbackTypeEnum(String code, String name) {
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
