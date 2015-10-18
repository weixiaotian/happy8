package com.happy8.args;

public class ReplayTimeLineReqArgs {
	private String publishUserId;
	private String commentedUserId;
	private long tlInfoId;
	private String txtContent;
	public String getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}
	public String getCommentedUserId() {
		return commentedUserId;
	}
	public void setCommentedUserId(String commentedUserId) {
		this.commentedUserId = commentedUserId;
	}
	public long getTlInfoId() {
		return tlInfoId;
	}
	public void setTlInfoId(long tlInfoId) {
		this.tlInfoId = tlInfoId;
	}
	public String getTxtContent() {
		return txtContent;
	}
	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}
	
}
