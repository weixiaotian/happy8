package com.happy8.args;

/**
 * 加好友请求
 *
 */
public class AddFriendReqArgs {
	private String userId;
	private String friendUserId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendUserId() {
		return friendUserId;
	}
	public void setFriendUserId(String friendUserId) {
		this.friendUserId = friendUserId;
	}
	
}
