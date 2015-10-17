package com.happy8.args;

/**
 * 棋牌室列表项
 *
 */
public class ClubItem {
	private int clubId;
	private String ownerId;
	private String addr;
	private String phone;
	private String sale;
	private String palyStyle;
	private double longitude;
	private double latitude;
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getPalyStyle() {
		return palyStyle;
	}
	public void setPalyStyle(String palyStyle) {
		this.palyStyle = palyStyle;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
