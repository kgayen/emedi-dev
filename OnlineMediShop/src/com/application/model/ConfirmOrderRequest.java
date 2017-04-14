package com.application.model;

import java.io.Serializable;

public class ConfirmOrderRequest implements Serializable {
	
	private static final long serialVersionUID = -5063536081885218933L;
	private String cashmemoNo;
	private String orderid;
	private String invoiceNo;
	private String invoiceId;
	private double orderprice;
	
	public double getOrderprice() {
		return orderprice;
	}
	public void setOrderprice(double orderprice) {
		this.orderprice = orderprice;
	}

	public String getCashmemoNo() {
		return cashmemoNo;
	}
	public void setCashmemoNo(String cashmemoNo) {
		this.cashmemoNo = cashmemoNo;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{orderid:"+orderid+",cashmemoNo:"+cashmemoNo+",invoiceNo:"+invoiceNo+",invoiceId:"+invoiceId+",orderprice:"+orderprice+"}";
	}
}
