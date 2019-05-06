package com.jinglitong.wallet.ddbapi.model.view;


import com.jinglitong.wallet.api.model.view.PageVO;

public class DdbIntegralWalletVo extends  PageVO{

	 	private Integer id;

	    private String zid;

	    private String custId;

	    private String inteName;

	    private String inteCname;

	    private Integer amount;

	    private String createTime;

	    private String updateTime;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getZid() {
			return zid;
		}

		public void setZid(String zid) {
			this.zid = zid;
		}

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getInteName() {
			return inteName;
		}

		public void setInteName(String inteName) {
			this.inteName = inteName;
		}

		public String getInteCname() {
			return inteCname;
		}

		public void setInteCname(String inteCname) {
			this.inteCname = inteCname;
		}

		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer amount) {
			this.amount = amount;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
	    
	    
}
