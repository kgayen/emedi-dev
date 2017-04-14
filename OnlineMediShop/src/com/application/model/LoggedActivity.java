package com.application.model;

public class LoggedActivity {
	
	private String userid;
	private String eventMessage;
	private String eventType;
	private String eventClassName;
	private String eventFunctionName;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getEventMessage() {
		return eventMessage;
	}
	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventClassName() {
		return eventClassName;
	}
	public void setEventClassName(String eventClassName) {
		this.eventClassName = eventClassName;
	}
	public String getEventFunctionName() {
		return eventFunctionName;
	}
	public void setEventFunctionName(String eventFunctionName) {
		this.eventFunctionName = eventFunctionName;
	}
}
