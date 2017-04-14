package com.application.model;

import java.sql.Timestamp;

public class OrderStatus {
	
	private String orderstatus;
	private long orderid;
	private String ordermodifier;
	private Timestamp orderActionTimestamp;
	private long orderStatusId;
	private String orderPlacedIP;
	
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public long getOrderid() {
		return orderid;
	}
	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}
	public String getOrdermodifier() {
		return ordermodifier;
	}
	public void setOrdermodifier(String ordermodifier) {
		this.ordermodifier = ordermodifier;
	}
	public Timestamp getOrderActionTimestamp() {
		return orderActionTimestamp;
	}
	public void setOrderActionTimestamp(Timestamp orderActionTimestamp) {
		this.orderActionTimestamp = orderActionTimestamp;
	}
	public long getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	public String getOrderPlacedIP() {
		return orderPlacedIP;
	}
	public void setOrderPlacedIP(String orderPlacedIP) {
		this.orderPlacedIP = orderPlacedIP;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{orderid:"+orderid+",orderstatus:"+orderstatus+",ordermodifier:"+ordermodifier+",orderPlacedIP:"+orderPlacedIP+"}";
	}
	
	
}
