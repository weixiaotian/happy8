package com.happy8.args;

import java.util.List;

/**
 * 找搭子信息列表项
 *
 */
public class FindBuddyInfoItem {
	private String userId;
	private long bdInfoId;
	private String infoContent;
	private List<FindBuddyCommentInfo> commentList;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getBdInfoId() {
		return bdInfoId;
	}
	public void setBdInfoId(long bdInfoId) {
		this.bdInfoId = bdInfoId;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public List<FindBuddyCommentInfo> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<FindBuddyCommentInfo> commentList) {
		this.commentList = commentList;
	}
}
