package com.application.dao;

import java.util.List;

import com.application.model.Notification;

public interface NotificationDao {
	public int insertNotoficationDetails(Notification notification);
	public List<Notification> getNotifications(String userid);
}
