package com.application.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public class CashmemoDetail implements Serializable {
	
	private static final long serialVersionUID = -238431730120848753L;
	private BigInteger orderid;
	private String cashmemono;
	private Timestamp cashmemoupdatetimestamp;
	private String invoiceno;
	private String invoiceid;
	private Timestamp invoicegeneratetimestamp;
	public BigInteger getOrderid() {
		return orderid;
	}
	public void setOrderid(BigInteger orderid) {
		this.orderid = orderid;
	}
	public String getCashmemono() {
		return cashmemono;
	}
	public void setCashmemono(String cashmemono) {
		this.cashmemono = cashmemono;
	}
	public Timestamp getCashmemoupdatetimestamp() {
		return cashmemoupdatetimestamp;
	}
	public void setCashmemoupdatetimestamp(Timestamp cashmemoupdatetimestamp) {
		this.cashmemoupdatetimestamp = cashmemoupdatetimestamp;
	}
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	public String getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(String invoiceid) {
		this.invoiceid = invoiceid;
	}
	public Timestamp getInvoicegeneratetimestamp() {
		return invoicegeneratetimestamp;
	}
	public void setInvoicegeneratetimestamp(Timestamp invoicegeneratetimestamp) {
		this.invoicegeneratetimestamp = invoicegeneratetimestamp;
	}
}
