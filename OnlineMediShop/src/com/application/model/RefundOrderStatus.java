package com.application.model;

import java.sql.Timestamp;

public class RefundOrderStatus {

	private String refundOrderstatus;
	private long refundOrderid;
	private String refundOrdermodifier;
	private Timestamp refundOrderActionTimestamp;
	private long refundOrderStatusId;
	private String refundOrderPlacedIP;
	private String refundRequestId;
	
	public String getRefundRequestId() {
		return refundRequestId;
	}
	public void setRefundRequestId(String refundRequestId) {
		this.refundRequestId = refundRequestId;
	}
	public String getRefundOrderstatus() {
		return refundOrderstatus;
	}
	public void setRefundOrderstatus(String refundOrderstatus) {
		this.refundOrderstatus = refundOrderstatus;
	}
	public long getRefundOrderid() {
		return refundOrderid;
	}
	public void setRefundOrderid(long refundOrderid) {
		this.refundOrderid = refundOrderid;
	}
	public String getRefundOrdermodifier() {
		return refundOrdermodifier;
	}
	public void setRefundOrdermodifier(String refundOrdermodifier) {
		this.refundOrdermodifier = refundOrdermodifier;
	}
	public Timestamp getRefundOrderActionTimestamp() {
		return refundOrderActionTimestamp;
	}
	public void setRefundOrderActionTimestamp(Timestamp refundOrderActionTimestamp) {
		this.refundOrderActionTimestamp = refundOrderActionTimestamp;
	}
	public long getRefundOrderStatusId() {
		return refundOrderStatusId;
	}
	public void setRefundOrderStatusId(long refundOrderStatusId) {
		this.refundOrderStatusId = refundOrderStatusId;
	}
	public String getRefundOrderPlacedIP() {
		return refundOrderPlacedIP;
	}
	public void setRefundOrderPlacedIP(String refundOrderPlacedIP) {
		this.refundOrderPlacedIP = refundOrderPlacedIP;
	}
	
}
