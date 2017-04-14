package com.application.model;

import java.util.List;

public class SMSModel {
	
	private String message;
	private String to;
	private String templateId;
	private String smsResponseFlag;
	private String smsErrorCode;
	private String responseMessage;
	private String requestString;
	private List<String> smsVariableProperties;
	private List<String> senderList;
	private String responseJSONMessage;
	
	
	public String getResponseJSONMessage() {
		return responseJSONMessage;
	}
	public void setResponseJSONMessage(String responseJSONMessage) {
		this.responseJSONMessage = responseJSONMessage;
	}
	public List<String> getSenderList() {
		return senderList;
	}
	public void setSenderList(List<String> senderList) {
		this.senderList = senderList;
	}
	public List<String> getSmsVariableProperties() {
		return smsVariableProperties;
	}
	public void setSmsVariableProperties(List<String> smsVariableProperties) {
		this.smsVariableProperties = smsVariableProperties;
	}
	public String getRequestString() {
		return requestString;
	}
	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public String getSmsResponseFlag() {
		return smsResponseFlag;
	}
	public void setSmsResponseFlag(String smsResponseFlag) {
		this.smsResponseFlag = smsResponseFlag;
	}
	public String getSmsErrorCode() {
		return smsErrorCode;
	}
	public void setSmsErrorCode(String smsErrorCode) {
		this.smsErrorCode = smsErrorCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
}
