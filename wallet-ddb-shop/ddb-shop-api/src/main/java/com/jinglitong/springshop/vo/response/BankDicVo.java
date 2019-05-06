package com.jinglitong.springshop.vo.response;

public class BankDicVo {

//  /**
//   * 业务id
//   */
//  private String zid;
  /**
   * 银行名
   */
  private String bankName;
  /**
   * 选择银行图标
   */
  private String selUrl;
  /**
   * 用户展示图标
   */
  private String showUrl;


  public String getBankName() {
      return bankName;
  }

  public void setBankName(String bankName) {
      this.bankName = bankName;
  }


  public String getSelUrl() {
		return selUrl;
	}

	public void setSelUrl(String selUrl) {
		this.selUrl = selUrl;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

}
