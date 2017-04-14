package com.application.model;

public class User {
	private String user_id;
	private String user_password;
	private String user_name;
	private String user_role;
	private String user_email;
	private String user_create_date;
	private String user_creator;
	private String user_mobile;
	private String user_address;
	private String user_pincode;
	
	
	public String getUser_mobile() {
		return user_mobile;
	}
	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	public String getUser_pincode() {
		return user_pincode;
	}
	public void setUser_pincode(String user_pincode) {
		this.user_pincode = user_pincode;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_create_date() {
		return user_create_date;
	}
	public void setUser_create_date(String user_create_date) {
		this.user_create_date = user_create_date;
	}
	public String getUser_creator() {
		return user_creator;
	}
	public void setUser_creator(String user_creator) {
		this.user_creator = user_creator;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[User Id: "+user_id+", User Name: "+user_name+", User_Role: "+user_role+", User_Mobile: "+user_mobile+", User_Pincode: "+user_pincode+", User_Address: "+user_address+", User_Creator: "+user_creator+"]";
	}
}
