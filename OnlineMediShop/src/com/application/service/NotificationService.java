package com.application.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.application.dao.NotificationDao;
import com.application.model.Notification;

public class NotificationService extends  BaseService implements NotificationDao {

	private JdbcTemplate template;
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	@Autowired
	Notification notification;
	
	public int insertNotoficationDetails(final Notification notification) {
		// TODO Auto-generated method stub
		logger.info("Executing Method insertNotoficationDetails().");
		final String query="INSERT INTO medishop.notification "
				+"(order_modifier_user,notification_text) VALUES"
				+ "(?,?)";
		int i = template.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(query);
			            ps.setString(1, notification.getNotifyuser());
			            ps.setString(2, notification.getNotificationText());
			            return ps;
			        }
			    });
		logger.info("Executing Method insertNotoficationDetails(). Result Value: "+i);
		return i;
	}
	
	public List<Notification> getNotifications(String userid){
		logger.info("Executing Method getNotification().");
		final String query = "select * from medishop.notification where order_modifier_user = '"+userid+"' and notification_read_status = 0  order by notification_id asc";
		logger.info("Executing Method getOrderStatus(). Query : "+query);
		return (List<Notification>) template.query(query,new ResultSetExtractor<List<Notification>>(){  
			public List<Notification> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Notification> list=new ArrayList<Notification>();  
				while(rs.next()){  
					notification=new Notification();					
					notification.setNotificationTimestamp(rs.getTimestamp("order_action_timestamp").getTime());
					notification.setNotificationText(rs.getString("notification_text"));
					notification.setNotificationReadStatus(rs.getInt("notification_read_status"));
					list.add(notification);  
				}  
				return list;  
			}  
		});
	}
	
	public int notificationBulkInsert(final List<Notification> notifications){
		logger.info("Executing Method notificationBulkInsert().");
		int insertStatus = 0;
		StringBuilder strBuilder = new StringBuilder("insert into `medishop`.`notification` (order_modifier_user,notification_text) values");
		for(Notification notification : notifications){
			strBuilder.append("(");
			strBuilder.append("'");
			strBuilder.append(notification.getNotifyuser());
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append(notification.getNotificationText());
			strBuilder.append("'");
			strBuilder.append(")");
			strBuilder.append(",");
		}
		strBuilder.delete(strBuilder.length()-1, strBuilder.length());
		strBuilder.append(";");
		final String query = strBuilder.toString();
		System.out.println("notificationBulkInsert : "+query);
		insertStatus = template.update(query);
		return insertStatus;
	}

}
