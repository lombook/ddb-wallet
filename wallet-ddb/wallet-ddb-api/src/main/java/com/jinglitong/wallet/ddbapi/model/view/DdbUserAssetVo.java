package com.jinglitong.wallet.ddbapi.model.view;

public class DdbUserAssetVo {
	/**
	 * 和移动端沟通：资产页面返回一个对象即可。
	 * 不用返回一个list
	 *    所以构造一个用户资产vo类，展示所有资产信息
	 */
	
//	合作社股权
	public String  cooperativeType;
	public String  cooperativeAmount;
//	育苗基地股权
	public String breedType;
	public String breedAmount;
//	宝分  
	public String integralType;
	public String integralAmount;
//	树贝
	public String bayTreeType;
	public String bayTreeAmount;
//	苗贝
	public String miaoBayType;
	public String miaoBayAmount;
//	现贝
	public String xianBayType;
	public String xianBayAmount;
//	物贝
	public String wuBayType;
	public String wuBayAmount;
//	冻结资产
//	public String freezType;
//	public String freezAmount;
//	金贝
	public String jinBayType;
	public String jinBayAmount;
	// 树
	public String treeType;
	public String treeNum;
	
	//股分
	public String guFenType = "gufen_all";
	public String guFenNum = "0";
	
	
	
	public String getTreeType() {
		return treeType;
	}
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	public String getTreeNum() {
		return treeNum;
	}
	public void setTreeNum(String treeNum) {
		this.treeNum = treeNum;
	}
	public String getJinBayType() {
		return jinBayType;
	}
	public void setJinBayType(String jinBayType) {
		this.jinBayType = jinBayType;
	}
	public String getJinBayAmount() {
		return jinBayAmount;
	}
	public void setJinBayAmount(String jinBayAmount) {
		this.jinBayAmount = jinBayAmount;
	}
	public String getCooperativeType() {
		return cooperativeType;
	}
	public void setCooperativeType(String cooperativeType) {
		this.cooperativeType = cooperativeType;
	}
	public String getCooperativeAmount() {
		return cooperativeAmount;
	}
	public void setCooperativeAmount(String cooperativeAmount) {
		this.cooperativeAmount = cooperativeAmount;
	}
	public String getBreedType() {
		return breedType;
	}
	public void setBreedType(String breedType) {
		this.breedType = breedType;
	}
	public String getBreedAmount() {
		return breedAmount;
	}
	public void setBreedAmount(String breedAmount) {
		this.breedAmount = breedAmount;
	}
	public String getIntegralType() {
		return integralType;
	}
	public void setIntegralType(String integralType) {
		this.integralType = integralType;
	}
	public String getIntegralAmount() {
		return integralAmount;
	}
	public void setIntegralAmount(String integralAmount) {
		this.integralAmount = integralAmount;
	}
	public String getBayTreeType() {
		return bayTreeType;
	}
	public void setBayTreeType(String bayTreeType) {
		this.bayTreeType = bayTreeType;
	}
	public String getBayTreeAmount() {
		return bayTreeAmount;
	}
	public void setBayTreeAmount(String bayTreeAmount) {
		this.bayTreeAmount = bayTreeAmount;
	}
	public String getMiaoBayType() {
		return miaoBayType;
	}
	public void setMiaoBayType(String miaoBayType) {
		this.miaoBayType = miaoBayType;
	}
	public String getMiaoBayAmount() {
		return miaoBayAmount;
	}
	public void setMiaoBayAmount(String miaoBayAmount) {
		this.miaoBayAmount = miaoBayAmount;
	}
	public String getXianBayType() {
		return xianBayType;
	}
	public void setXianBayType(String xianBayType) {
		this.xianBayType = xianBayType;
	}
	public String getXianBayAmount() {
		return xianBayAmount;
	}
	public void setXianBayAmount(String xianBayAmount) {
		this.xianBayAmount = xianBayAmount;
	}
	public String getWuBayType() {
		return wuBayType;
	}
	public void setWuBayType(String wuBayType) {
		this.wuBayType = wuBayType;
	}
	public String getWuBayAmount() {
		return wuBayAmount;
	}
	public void setWuBayAmount(String wuBayAmount) {
		this.wuBayAmount = wuBayAmount;
	}
	public String getGuFenType() {
		return guFenType;
	}
	public void setGuFenType(String guFenType) {
		this.guFenType = guFenType;
	}
	public String getGuFenNum() {
		return guFenNum;
	}
	public void setGuFenNum(String guFenNum) {
		this.guFenNum = guFenNum;
	}
	
//	public String getFreezType() {
//		return freezType;
//	}
//	public void setFreezType(String freezType) {
//		this.freezType = freezType;
//	}
//	public String getFreezAmount() {
//		return freezAmount;
//	}
//	public void setFreezAmount(String freezAmount) {
//		this.freezAmount = freezAmount;
//	}
	
	
	
	
	 

}
