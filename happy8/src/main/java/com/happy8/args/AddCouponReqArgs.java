package com.happy8.args;

import java.util.Date;

public class AddCouponReqArgs {
	private String userId;
	private int type;
	private int disCount;
	private double value;
	private double startAmout;
	private Date expireTime;
	
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
}
