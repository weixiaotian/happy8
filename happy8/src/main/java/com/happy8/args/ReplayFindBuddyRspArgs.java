package com.happy8.args;

/**
 * 回复找搭子应答
 *
 */
public class ReplayFindBuddyRspArgs {
	private long commentId;
	
	private FindBuddyCommentInfo item;

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public FindBuddyCommentInfo getItem() {
		return item;
	}

	public void setItem(FindBuddyCommentInfo item) {
		this.item = item;
	}
}
