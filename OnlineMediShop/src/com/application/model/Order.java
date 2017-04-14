package com.application.model;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements Serializable {
	
	private static final long serialVersionUID = -5829161289677675820L;
	private String orderid;
	private String ordercreator;
	private String orderdetails;
	InputStream orderpecimage;
	private String orderstatus;
	private BigDecimal orderprice;
	private Timestamp orderlastmodify;
	private long orderSellerId;
	private Timestamp orderCreateDate;
	private String cancellationCmd;
	private int emergencyFlag;
	private String shippingAddress;
	private BigDecimal emergencyPrice;
	private String orderPincode;
	private BigDecimal orderDiscountAmount;
	private BigDecimal orderDeliveryAmount;
	
	
	public Timestamp getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Timestamp orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	public long getOrderSellerId() {
		return orderSellerId;
	}
	public void setOrderSellerId(long orderSellerId) {
		this.orderSellerId = orderSellerId;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrdercreator() {
		return ordercreator;
	}
	public void setOrdercreator(String ordercreator) {
		this.ordercreator = ordercreator;
	}
	public String getOrderdetails() {
		return orderdetails;
	}
	public void setOrderdetails(String orderdetails) {
		this.orderdetails = orderdetails;
	}
	public InputStream getOrderpecimage() {
		return orderpecimage;
	}
	public void setOrderpecimage(InputStream orderpecimage) {
		this.orderpecimage = orderpecimage;
	}
	public String getOrderstatus() {
		return orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}
	public BigDecimal getOrderprice() {
		return orderprice;
	}
	public void setOrderprice(BigDecimal orderprice) {
		this.orderprice = orderprice;
	}
	public Timestamp getOrderlastmodify() {
		return orderlastmodify;
	}
	public void setOrderlastmodify(Timestamp orderlastmodify) {
		this.orderlastmodify = orderlastmodify;
	}
	public String getCancellationCmd() {
		return cancellationCmd;
	}
	public void setCancellationCmd(String cancellationCmd) {
		this.cancellationCmd = cancellationCmd;
	}
	public int getEmergencyFlag() {
		return emergencyFlag;
	}
	public void setEmergencyFlag(int emergencyFlag) {
		this.emergencyFlag = emergencyFlag;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public BigDecimal getEmergencyPrice() {
		return emergencyPrice;
	}
	public void setEmergencyPrice(BigDecimal emergencyPrice) {
		this.emergencyPrice = emergencyPrice;
	}
	
	public String getOrderPincode() {
		return orderPincode;
	}
	public void setOrderPincode(String orderPincode) {
		this.orderPincode = orderPincode;
	}
	public BigDecimal getOrderDiscountAmount() {
		return orderDiscountAmount;
	}
	public void setOrderDiscountAmount(BigDecimal orderDiscountAmount) {
		this.orderDiscountAmount = orderDiscountAmount;
	}
	public BigDecimal getOrderDeliveryAmount() {
		return orderDeliveryAmount;
	}
	public void setOrderDeliveryAmount(BigDecimal orderDeliveryAmount) {
		this.orderDeliveryAmount = orderDeliveryAmount;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Order Details: [Orderid:"+orderid+",ordercreator:"+ordercreator+",orderdetails:"+orderdetails+",orderstatus:"+orderstatus
				+",orderprice:"+orderprice+",orderlastmodify:"+orderlastmodify+"]";
	}
}
