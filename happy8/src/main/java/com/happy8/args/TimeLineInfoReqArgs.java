package com.happy8.args;

/**
 * 发布牌友圈请求
 *
 */
public class TimeLineInfoReqArgs {
	private String userId;
	private String infoContent;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	
}
