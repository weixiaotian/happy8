package com.happy8.args;

/**
 * 收藏棋牌室请求
 *
 */
public class AddFavoriteReqArgs {
	private String userId;
	private int clubId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
}
