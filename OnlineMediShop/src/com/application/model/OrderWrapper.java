package com.application.model;

import java.util.List;

public class OrderWrapper {
	private User user;
	private Seller seller;
	private List<Notification> notifications;
	private List<OrderStatus> orderstatuslist;
	private Order order;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Seller getSeller() {
		return seller;
	}
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	public List<OrderStatus> getOrderstatus() {
		return orderstatuslist;
	}
	public void setOrderstatus(List<OrderStatus> orderstatus) {
		this.orderstatuslist = orderstatus;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
}
