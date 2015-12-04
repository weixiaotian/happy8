package com.happy8.args;

import java.util.List;

public class OrderTableReqArgs {
	private List<OrderDetailItem> detail;
	private double amount;
	

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public List<OrderDetailItem> getDetail() {
		return detail;
	}

	public void setDetail(List<OrderDetailItem> detail) {
		this.detail = detail;
	}
}
