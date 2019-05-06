package com.jinglitong.wallet.api.model.view;

import java.util.Date;

import com.jinglitong.wallet.api.model.IAppIdModel;

import lombok.Data;

@Data
public class CertificateVO  implements IAppIdModel{

	private String custName;

	private String identityNo;

	private String identityType;

	private Long provinceId;

	private Long cityId;
	
	private String frontUrl;
	
	private String backUrl;

	/**
	 * 数据收集身份认证
	 * @return
	 */
	//主键id
	private Long id;

	private String zid;
	//户认证时创建时间
	private String timesTamp;
	//用户名
	private String account;
	//钱包编码
	private String appId;
	//经纬度
	private String gps;
	//设备id
	private String deviceId;
	//创建时间
	private Date createTime;
	//审核通过状态
	private boolean flag;
	//用户id
	private String custId;

}

