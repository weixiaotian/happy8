package com.happy8.args;

/**
 * 找搭子评论
 *
 */
public class FindBuddyCommentInfo {
	private long commentId;
	private long bdInfoId;
	private String commentText;
	private String publishUserId;
	private String signature;
	private String avatarUrl;
	private String dateTime;
	//private String commentedUserId;
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public String getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}
//	public String getCommentedUserId() {
//		return commentedUserId;
//	}
//	public void setCommentedUserId(String commentedUserId) {
//		this.commentedUserId = commentedUserId;
//	}
	public long getBdInfoId() {
		return bdInfoId;
	}
	public void setBdInfoId(long bdInfoId) {
		this.bdInfoId = bdInfoId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
