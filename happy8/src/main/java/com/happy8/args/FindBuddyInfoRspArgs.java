package com.happy8.args;

/**
 * 找搭子应答
 *
 */
public class FindBuddyInfoRspArgs {
	private long bdInfoId;
	
	private FindBuddyInfoItem fdItem;
	
	public long getBdInfoId() {
		return bdInfoId;
	}

	public void setBdInfoId(long bdInfoId) {
		this.bdInfoId = bdInfoId;
	}

	public FindBuddyInfoItem getFdItem() {
		return fdItem;
	}

	public void setFdItem(FindBuddyInfoItem fdItem) {
		this.fdItem = fdItem;
	}
	
	
}
