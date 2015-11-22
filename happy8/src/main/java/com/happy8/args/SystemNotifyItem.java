package com.happy8.args;

public class SystemNotifyItem {
	private int snId;
	private String title;
	private String content;
	private String sendTime;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getSnId() {
		return snId;
	}
	public void setSnId(int snId) {
		this.snId = snId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
}
