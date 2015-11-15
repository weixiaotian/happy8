package com.happy8.args;

/**
 * 回复找搭子应答
 *
 */
public class ReplayFindBuddyRspArgs {
	private long commentId;
	
	private FindBuddyInfoItem item;

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public FindBuddyInfoItem getItem() {
		return item;
	}

	public void setItem(FindBuddyInfoItem item) {
		this.item = item;
	}
}
