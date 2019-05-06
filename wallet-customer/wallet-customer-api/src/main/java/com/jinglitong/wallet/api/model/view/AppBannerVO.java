package com.jinglitong.wallet.api.model.view;

import com.jinglitong.wallet.api.model.IAppIdModel;
import lombok.Data;

@Data
public class AppBannerVO extends PageVO implements IAppIdModel{

	private Integer id;

	private String banId;
	
	private String name;
	
	private String imgUrl;
	
	private String orderId;
	
	private String clickType;

	private String clickId;

	private String remark;

	private String state;

	private String appId;

	private String walletName;

	private String createTime;
	

}
