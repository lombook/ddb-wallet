package com.jinglitong.wallet.ddbapi.model.view;

/**
 * 游戏向钱包后台同步信息：好友增加减少通知
 * 
 * @author liangtf
 * 
 *
 */
public class FriendsChangeNoticeVo {
	
	private String flowId;
	
	private String userId;
	
	private String friendId;
	
	private String action;
	
	private String nonceStr;
	
	private String sign;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	

}
