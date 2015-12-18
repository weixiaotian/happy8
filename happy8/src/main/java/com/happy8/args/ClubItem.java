package com.happy8.args;

/**
 * 棋牌室列表项
 *
 */
public class ClubItem {
	private int clubId;
	private String ownerId;
	private String name;
	private String addr;
	private String phone;
	private double sale;
	private String playStyle;
	private double longitude;
	private double latitude;
	private String clubImageUrl;
	private String clubviewurl;
	private int status;
	private boolean isMyFavorite = false;
	
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
	public double getSale() {
		return sale;
	}
	public void setSale(double sale) {
		this.sale = sale;
	}
	public String getPlayStyle() {
		return playStyle;
	}
	public void setPlayStyle(String playStyle) {
		this.playStyle = playStyle;
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
	public String getClubImageUrl() {
		return clubImageUrl;
	}
	public void setClubImageUrl(String clubImageUrl) {
		this.clubImageUrl = clubImageUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClubviewurl() {
		return clubviewurl;
	}
	public void setClubviewurl(String clubviewurl) {
		this.clubviewurl = clubviewurl;
	}
	public boolean isMyFavorite() {
		return isMyFavorite;
	}
	public void setMyFavorite(boolean isMyFavorite) {
		this.isMyFavorite = isMyFavorite;
	}
}
