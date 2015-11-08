package com.happy8.args;

public class CouponItem {
	private String couponId;
	private int type;
	private int disCount;
	private double value;
	private double startAmout;
	private int status;
	
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDisCount() {
		return disCount;
	}
	public void setDisCount(int disCount) {
		this.disCount = disCount;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getStartAmout() {
		return startAmout;
	}
	public void setStartAmout(double startAmout) {
		this.startAmout = startAmout;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
