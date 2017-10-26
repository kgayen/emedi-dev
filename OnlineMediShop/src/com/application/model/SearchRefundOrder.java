package com.application.model;

import java.util.List;

public class SearchRefundOrder {
	
	private String refundSearchType;
	private long fromDate;
	private long toDate;
	private List<String> refundOrderIdList;
	private List<String> refundOrderStatusList;
	private String userId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRefundSearchType() {
		return refundSearchType;
	}
	public void setRefundSearchType(String refundSearchType) {
		this.refundSearchType = refundSearchType;
	}
	public long getFromDate() {
		return fromDate;
	}
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	public long getToDate() {
		return toDate;
	}
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	public List<String> getRefundOrderIdList() {
		return refundOrderIdList;
	}
	public void setRefundOrderIdList(List<String> refundOrderIdList) {
		this.refundOrderIdList = refundOrderIdList;
	}
	public List<String> getRefundOrderStatusList() {
		return refundOrderStatusList;
	}
	public void setRefundOrderStatusList(List<String> refundOrderStatusList) {
		this.refundOrderStatusList = refundOrderStatusList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{refundSearchType : "+refundSearchType+",fromDate : "+fromDate+",toDate : "+toDate+",userId : "+userId+"}";
	}
}
