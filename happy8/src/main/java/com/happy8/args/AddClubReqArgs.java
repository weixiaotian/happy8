package com.happy8.args;

/**
 * 增加棋牌室请求
 *
 */
public class AddClubReqArgs {
	private String ownerId;
	private String addr;
	private String name;
	private String phone;
	private double sale;
	private String palyStyle;
	private double longitude;
	private double latitude;
	private String clubImageUrl;
	private String clubviewurl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPalyStyle() {
		return palyStyle;
	}
	public void setPalyStyle(String palyStyle) {
		this.palyStyle = palyStyle;
	}
	public String getClubImageUrl() {
		return clubImageUrl;
	}
	public void setClubImageUrl(String clubImageUrl) {
		this.clubImageUrl = clubImageUrl;
	}
	public String getClubviewurl() {
		return clubviewurl;
	}
	public void setClubviewurl(String clubviewurl) {
		this.clubviewurl = clubviewurl;
	}
}
