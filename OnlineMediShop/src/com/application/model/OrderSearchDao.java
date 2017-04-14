package com.application.model;

import java.util.List;

public class OrderSearchDao {
	
	private String orderSearchType;
	private long fromDate;
	private long toDate;
	private List<String> orderIdList;
	private List<String> orderStatusList;
	private List<String> orderFetchedBy;
	
	public String getOrderSearchType() {
		return orderSearchType;
	}
	public void setOrderSearchType(String orderSearchType) {
		this.orderSearchType = orderSearchType;
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
	public List<String> getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(List<String> orderIdList) {
		this.orderIdList = orderIdList;
	}
	public List<String> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(List<String> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	public List<String> getOrderFetchedBy() {
		return orderFetchedBy;
	}
	public void setOrderFetchedBy(List<String> orderFetchedBy) {
		this.orderFetchedBy = orderFetchedBy;
	}
	

}
