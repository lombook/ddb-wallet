package com.jinglitong.springshop.enumeration;

public enum FeedbackTypeEnum {
	
	ORDER_FEEDBACK("1","订单问题"),LOGISTICS_FEEDBACK("2","物流问题"),
	PRODUCT_FEEDBACK("3","产品反馈"),PRODUCT_APPLY("4","商品申请");
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
