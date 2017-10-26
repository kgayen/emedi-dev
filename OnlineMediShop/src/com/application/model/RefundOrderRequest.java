package com.application.model;

import java.sql.Timestamp;

public class RefundOrderRequest {
	
	private String refundOrderid;
	private String refundInitiateUser;
	private String refundOrderStatus;
	private Timestamp refundInitiateTime;
	private String reundReason;
	private String refundCashmemono;
	private double refundAmount;
	private String refundSellerCommt;
	private String refundRequestId;
	private String requestedHostIP;
	private String refundNotificattionText;
	private String sellerID;
	private String sellerComments;
	private Timestamp refundLastModifiedTime;
	
	public String getSellerComments() {
		return sellerComments;
	}
	public void setSellerComments(String sellerComments) {
		this.sellerComments = sellerComments;
	}
	public Timestamp getRefundLastModifiedTime() {
		return refundLastModifiedTime;
	}
	public void setRefundLastModifiedTime(Timestamp refundLastModifiedTime) {
		this.refundLastModifiedTime = refundLastModifiedTime;
	}
	public String getSellerID() {
		return sellerID;
	}
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	public String getRefundNotificattionText() {
		return refundNotificattionText;
	}
	public void setRefundNotificattionText(String refundNotificattionText) {
		this.refundNotificattionText = refundNotificattionText;
	}
	public String getRequestedHostIP() {
		return requestedHostIP;
	}
	public void setRequestedHostIP(String requestedHostIP) {
		this.requestedHostIP = requestedHostIP;
	}
	public String getRefundRequestId() {
		return refundRequestId;
	}
	public void setRefundRequestId(String refundRequestId) {
		this.refundRequestId = refundRequestId;
	}
	public String getRefundSellerCommt() {
		return refundSellerCommt;
	}
	public void setRefundSellerCommt(String refundSellerCommt) {
		this.refundSellerCommt = refundSellerCommt;
	}
	public String getRefundOrderid() {
		return refundOrderid;
	}
	public void setRefundOrderid(String refundOrderid) {
		this.refundOrderid = refundOrderid;
	}
	public String getRefundInitiateUser() {
		return refundInitiateUser;
	}
	public void setRefundInitiateUser(String refundInitiateUser) {
		this.refundInitiateUser = refundInitiateUser;
	}
	public String getRefundOrderStatus() {
		return refundOrderStatus;
	}
	public void setRefundOrderStatus(String refundOrderStatus) {
		this.refundOrderStatus = refundOrderStatus;
	}
	public Timestamp getRefundInitiateTime() {
		return refundInitiateTime;
	}
	public void setRefundInitiateTime(Timestamp refundInitiateTime) {
		this.refundInitiateTime = refundInitiateTime;
	}
	public String getReundReason() {
		return reundReason;
	}
	public void setReundReason(String reundReason) {
		this.reundReason = reundReason;
	}
	public String getRefundCashmemono() {
		return refundCashmemono;
	}
	public void setRefundCashmemono(String refundCashmemono) {
		this.refundCashmemono = refundCashmemono;
	}
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{\"refundOrderid\":\""+refundOrderid+"\",\"refundOrderStatus\":\""+refundOrderStatus+"\",\"refundRequestId\":\""+refundRequestId+"\",\"sellerComments\":\""+sellerComments+"\"}";
	}
}
