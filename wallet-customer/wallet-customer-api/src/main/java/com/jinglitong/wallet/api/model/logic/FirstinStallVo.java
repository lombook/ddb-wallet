package com.jinglitong.wallet.api.model.logic;

import java.io.Serializable;


/**
 * 
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2018-06-20 18:44:04
 */
public class FirstinStallVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String packageName;
	private String appName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
