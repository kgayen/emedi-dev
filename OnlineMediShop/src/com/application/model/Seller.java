package com.application.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Seller implements Serializable {
	
	private static final long serialVersionUID = 3918828471774663250L;
	private String sellerShopName;
	private double sellerComission;
	private int sellerPriority;
	private String sellerStatus;
	private String sellerDeliveryPincode;
	private Timestamp sellerCreateDate;
	private Timestamp sellerLastModifyDate;
	private Timestamp sellerExpiryDate;
	private String sellerAddress;
	private String sellerRegistrationNo;
	private long sellerid;
	private double sellerDiscount;
	private BigDecimal sellerEmergencyPrice;
	private BigDecimal sellerDeliveryPrice;
	private String sellerEmailId;
	private String sellerMobileNo;
	private String taxType;
	private String taxCategory;
	
	
	public String getTaxCategory() {
		return taxCategory;
	}
	public void setTaxCategory(String taxCategory) {
		this.taxCategory = taxCategory;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getSellerEmailId() {
		return sellerEmailId;
	}
	public void setSellerEmailId(String sellerEmailId) {
		this.sellerEmailId = sellerEmailId;
	}
	public String getSellerMobileNo() {
		return sellerMobileNo;
	}
	public void setSellerMobileNo(String sellerMobileNo) {
		this.sellerMobileNo = sellerMobileNo;
	}
	public long getSellerid() {
		return sellerid;
	}
	public void setSellerid(long sellerid) {
		this.sellerid = sellerid;
	}
	public Timestamp getSellerCreateDate() {
		return sellerCreateDate;
	}
	public void setSellerCreateDate(Timestamp sellerCreateDate) {
		this.sellerCreateDate = sellerCreateDate;
	}
	public String getSellerShopName() {
		return sellerShopName;
	}
	public void setSellerShopName(String sellerShopName) {
		this.sellerShopName = sellerShopName;
	}
	public double getSellerComission() {
		return sellerComission;
	}
	public void setSellerComission(double sellerComission) {
		this.sellerComission = sellerComission;
	}
	public int getSellerPriority() {
		return sellerPriority;
	}
	public void setSellerPriority(int sellerPriority) {
		this.sellerPriority = sellerPriority;
	}
	public String getSellerStatus() {
		return sellerStatus;
	}
	public void setSellerStatus(String sellerStatus) {
		this.sellerStatus = sellerStatus;
	}
	public String getSellerDeliveryPincode() {
		return sellerDeliveryPincode;
	}
	public void setSellerDeliveryPincode(String sellerDeliveryPincode) {
		this.sellerDeliveryPincode = sellerDeliveryPincode;
	}
	public Timestamp getSellerLastModifyDate() {
		return sellerLastModifyDate;
	}
	public void setSellerLastModifyDate(Timestamp sellerLastModifyDate) {
		this.sellerLastModifyDate = sellerLastModifyDate;
	}
	public Timestamp getSellerExpiryDate() {
		return sellerExpiryDate;
	}
	public void setSellerExpiryDate(Timestamp sellerExpiryDate) {
		this.sellerExpiryDate = sellerExpiryDate;
	}
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerRegistrationNo() {
		return sellerRegistrationNo;
	}
	public void setSellerRegistrationNo(String sellerRegistrationNo) {
		this.sellerRegistrationNo = sellerRegistrationNo;
	}
	public double getSellerDiscount() {
		return sellerDiscount;
	}
	public void setSellerDiscount(double sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}
	public BigDecimal getSellerEmergencyPrice() {
		return sellerEmergencyPrice;
	}
	public void setSellerEmergencyPrice(BigDecimal sellerEmergencyPrice) {
		this.sellerEmergencyPrice = sellerEmergencyPrice;
	}
	public BigDecimal getSellerDeliveryPrice() {
		return sellerDeliveryPrice;
	}
	public void setSellerDeliveryPrice(BigDecimal sellerDeliveryPrice) {
		this.sellerDeliveryPrice = sellerDeliveryPrice;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[SellerName: "+sellerShopName+", Seller Pincode: "+sellerDeliveryPincode+", Seller Expiry Date: "+sellerExpiryDate+", Seller Status: "+sellerStatus+"]";
	}
}
