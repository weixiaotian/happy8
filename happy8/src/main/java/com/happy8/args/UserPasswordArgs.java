package com.happy8.args;

/**
 * 验证密码修改密码请求
 *
 */
public class UserPasswordArgs {
	private String userId;
	private String password;
	private String pushtoken;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPushtoken() {
		return pushtoken;
	}
	public void setPushtoken(String pushtoken) {
		this.pushtoken = pushtoken;
	}
	
}
