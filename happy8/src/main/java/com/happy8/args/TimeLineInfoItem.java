package com.happy8.args;

import java.util.List;

/**
 * 牌友圈信息列表项
 *
 */
public class TimeLineInfoItem {
	private String userId;
	private long tlInfoId;
	private String infoContent;
	private List<TimeLineCommentInfoItem> commentList;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getTlInfoId() {
		return tlInfoId;
	}
	public void setTlInfoId(long tlInfoId) {
		this.tlInfoId = tlInfoId;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public List<TimeLineCommentInfoItem> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<TimeLineCommentInfoItem> commentList) {
		this.commentList = commentList;
	}
}
