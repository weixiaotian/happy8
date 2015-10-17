package com.happy8.args;

/**
 * 牌友圈评论列表项
 *
 */
public class TimeLineCommentInfoItem {
	private long commentId;
	private long tlInfoId;
	private String commentText;
	private String publishUserId;
	private String commentedUserId;
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getTlInfoId() {
		return tlInfoId;
	}
	public void setTlInfoId(long tlInfoId) {
		this.tlInfoId = tlInfoId;
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
	public String getCommentedUserId() {
		return commentedUserId;
	}
	public void setCommentedUserId(String commentedUserId) {
		this.commentedUserId = commentedUserId;
	}
	
}
