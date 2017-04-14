package com.application.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class RefundOrder {
	
	private String refundOrderid;
	private String cashmemoNo;
	private String refunRequestNo;
	private Timestamp refundOrderlastmodify;
	private Timestamp refundInitiateTime;
	private String refundOrderStatus;
	private BigDecimal refundAmount;
	private String refundReason;
	private String refundInitiateUser;
	private String refundOrderSellerComt;
	private List<RefundOrderStatus> refundOrderStatusList;
	private String sellerId;
	private String initiatorMobileNo;
	private String initiatorAddress;
	private String initiatorEmailAddress;
	
	
	public String getInitiatorMobileNo() {
		return initiatorMobileNo;
	}
	public void setInitiatorMobileNo(String initiatorMobileNo) {
		this.initiatorMobileNo = initiatorMobileNo;
	}
	public String getInitiatorAddress() {
		return initiatorAddress;
	}
	public void setInitiatorAddress(String initiatorAddress) {
		this.initiatorAddress = initiatorAddress;
	}
	public String getInitiatorEmailAddress() {
		return initiatorEmailAddress;
	}
	public void setInitiatorEmailAddress(String initiatorEmailAddress) {
		this.initiatorEmailAddress = initiatorEmailAddress;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public List<RefundOrderStatus> getRefundOrderStatusList() {
		return refundOrderStatusList;
	}
	public void setRefundOrderStatusList(List<RefundOrderStatus> refundOrderStatusList) {
		this.refundOrderStatusList = refundOrderStatusList;
	}
	public String getRefundOrderid() {
		return refundOrderid;
	}
	public void setRefundOrderid(String refundOrderid) {
		this.refundOrderid = refundOrderid;
	}
	public String getCashmemoNo() {
		return cashmemoNo;
	}
	public void setCashmemoNo(String cashmemoNo) {
		this.cashmemoNo = cashmemoNo;
	}
	public String getRefunRequestNo() {
		return refunRequestNo;
	}
	public void setRefunRequestNo(String refunRequestNo) {
		this.refunRequestNo = refunRequestNo;
	}
	public Timestamp getRefundOrderlastmodify() {
		return refundOrderlastmodify;
	}
	public void setRefundOrderlastmodify(Timestamp refundOrderlastmodify) {
		this.refundOrderlastmodify = refundOrderlastmodify;
	}
	public Timestamp getRefundInitiateTime() {
		return refundInitiateTime;
	}
	public void setRefundInitiateTime(Timestamp refundInitiateTime) {
		this.refundInitiateTime = refundInitiateTime;
	}
	public String getRefundOrderStatus() {
		return refundOrderStatus;
	}
	public void setRefundOrderStatus(String refundOrderStatus) {
		this.refundOrderStatus = refundOrderStatus;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getRefundInitiateUser() {
		return refundInitiateUser;
	}
	public void setRefundInitiateUser(String refundInitiateUser) {
		this.refundInitiateUser = refundInitiateUser;
	}
	public String getRefundOrderSellerComt() {
		return refundOrderSellerComt;
	}
	public void setRefundOrderSellerComt(String refundOrderSellerComt) {
		this.refundOrderSellerComt = refundOrderSellerComt;
	}
}
