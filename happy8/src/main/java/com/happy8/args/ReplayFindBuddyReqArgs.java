package com.happy8.args;

/**
 * 回复找搭子请求
 * @author Administrator
 *
 */
public class ReplayFindBuddyReqArgs {
	private String publishUserId;
	private String commentedUserId;
	private long bdInfoId;
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
	public long getBdInfoId() {
		return bdInfoId;
	}
	public void setBdInfoId(long bdInfoId) {
		this.bdInfoId = bdInfoId;
	}
	public String getTxtContent() {
		return txtContent;
	}
	public void setTxtContent(String txtContent) {
		this.txtContent = txtContent;
	}
}
