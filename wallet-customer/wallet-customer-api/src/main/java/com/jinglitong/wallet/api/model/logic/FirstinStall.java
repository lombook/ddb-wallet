package com.jinglitong.wallet.api.model.logic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2018-06-20 18:44:04
 */
public class FirstinStall implements Serializable {
	private static final long serialVersionUID = 1L;


	//主键id
	private Long id;
	//装机时间
	private String loadingTime;
	//经纬度
	private String gps;
	//app版本号
	private String appVersion;
	//设备操作系统及版本号
	private String deviceos;
	//设备生产厂商
	private String firm;
	//设备id
	private String deviceId;
	//来源（安卓/ios）
	private String osType;
	//设备相关配置参数列表
	private String deviceHd;
	//已安装的其他应用列表
	private List<FirstinStallVo> appList;
	//钱包编码
	private String appId;
	//创建时间
	private Date createTime;

	/**
	 * 设置：主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：装机时间
	 */
	public void setLoadingTime(String loadingTime) {
		this.loadingTime = loadingTime;
	}
	/**
	 * 获取：装机时间
	 */
	public String getLoadingTime() {
		return loadingTime;
	}
	/**
	 * 设置：经纬度
	 */
	public void setGps(String gps) {
		this.gps = gps;
	}
	/**
	 * 获取：经纬度
	 */
	public String getGps() {
		return gps;
	}
	/**
	 * 设置：app版本号
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	/**
	 * 获取：app版本号
	 */
	public String getAppVersion() {
		return appVersion;
	}
	/**
	 * 设置：设备操作系统及版本号
	 */
	public void setDeviceos(String deviceos) {
		this.deviceos = deviceos;
	}
	/**
	 * 获取：设备操作系统及版本号
	 */
	public String getDeviceos() {
		return deviceos;
	}
	/**
	 * 设置：设备生产厂商
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}
	/**
	 * 获取：设备生产厂商
	 */
	public String getFirm() {
		return firm;
	}
	/**
	 * 设置：设备id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * 获取：设备id
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * 设置：来源（安卓/ios）
	 */
	public void setOsType(String osType) {
		this.osType = osType;
	}
	/**
	 * 获取：来源（安卓/ios）
	 */
	public String getOsType() {
		return osType;
	}
	/**
	 * 设置：已安装的其他设备列表
	 */
	public void setAppList(List appList) {
		this.appList = appList;
	}
	/**
	 * 获取：已安装的其他设备列表
	 */
	public List getAppList() {
		return appList;
	}
	/**
	 * 设置：钱包编码
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * 获取：钱包编码
	 */
	public String getAppId() {
		return appId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public String getDeviceHd() {
		return deviceHd;
	}

	public void setDeviceHd(String deviceHd) {
		this.deviceHd = deviceHd;
	}
}
