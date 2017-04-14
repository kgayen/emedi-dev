package com.application.model;

public class Notification {
	
	private long notificationId;
	private long notificationTimestamp;
	private String notifyuser;
	private String notificationText;
	private int notificationReadStatus;
	
	public long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}
	public long getNotificationTimestamp() {
		return notificationTimestamp;
	}
	public void setNotificationTimestamp(long notificationTimestamp) {
		this.notificationTimestamp = notificationTimestamp;
	}
	public String getNotifyuser() {
		return notifyuser;
	}
	public void setNotifyuser(String notifyuser) {
		this.notifyuser = notifyuser;
	}
	public String getNotificationText() {
		return notificationText;
	}
	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	public int getNotificationReadStatus() {
		return notificationReadStatus;
	}
	public void setNotificationReadStatus(int notificationReadStatus) {
		this.notificationReadStatus = notificationReadStatus;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{'notifyuser':"+notifyuser+",'notificationText':"+notificationText+"}";
	}
	
	
}
