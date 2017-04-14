package com.application.model;

import java.io.Serializable;

public class Invoice implements Serializable{

	private static final long serialVersionUID = -8942654535060547183L;
	
	private Order order;
	private Seller seller;
	private OrderStatus orderstatus;
	private CashmemoDetail cashmemodetail;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Seller getSeller() {
		return seller;
	}
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	public OrderStatus getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(OrderStatus orderstatus) {
		this.orderstatus = orderstatus;
	}
	public CashmemoDetail getCashmemodetail() {
		return cashmemodetail;
	}
	public void setCashmemodetail(CashmemoDetail cashmemodetail) {
		this.cashmemodetail = cashmemodetail;
	}
}
