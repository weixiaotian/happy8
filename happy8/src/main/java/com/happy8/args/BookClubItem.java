package com.happy8.args;

/**
 * 预定棋牌室列表项
 */
public class BookClubItem {
	private long bookId;
	private long clubId;
	private String userId ;
	private int tableIndex;
	private int chairIndex;
	private String startTime;
	private int duration;
	
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public long getClubId() {
		return clubId;
	}
	public void setClubId(long clubId) {
		this.clubId = clubId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getTableIndex() {
		return tableIndex;
	}
	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}
	public int getChairIndex() {
		return chairIndex;
	}
	public void setChairIndex(int chairIndex) {
		this.chairIndex = chairIndex;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
