package com.happy8.args;

/**
 * 删除棋牌室
 */
public class DeleteClubReqArgs {
	private String ownerId;
	private int clubId;
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	
}
