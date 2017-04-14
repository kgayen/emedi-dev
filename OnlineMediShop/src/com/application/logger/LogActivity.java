package com.application.logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.application.dao.LogActivityDao;
import com.application.exception.CustomGenericException;

public class LogActivity implements LogActivityDao {
	private JdbcTemplate template;
	private static final Logger logger = LoggerFactory.getLogger(LogActivity.class);
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}

	public void saveLogActivity(final String userid,final String eventMessage,final String eventType,final String eventClassName,
			final String eventFunctionName) {
		logger.info("Executing Method saveLogActivity().");
		final String query="INSERT INTO medishop.logactivity"
				+"(event_user_id,event_message,event_type,event_class_name,event_function_name) VALUES"
				+ "(?,?,?,?,?)";
		int i = template.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(query);
			            ps.setString(1, userid);
			            ps.setString(2, eventMessage);
			            ps.setString(3, eventType);
			            ps.setString(4, eventClassName);
			            ps.setString(5, eventFunctionName);
			            return ps;
			        }
			    });
		logger.info("Executing Method saveLogActivity(). Result Value: "+i);
		
	}

	public void saveErrorLog(final CustomGenericException customGenericException) {
		logger.info("Executing Method saveErrorLog().");
		final String query="INSERT INTO medishop.exception_log"
				+"(error_message,error_method_name,error_code,user_id,error_class_name) VALUES"
				+ "(?,?,?,?,?)";
		int i = template.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(query);
			            ps.setString(1, customGenericException.getErrorMsg());
			            ps.setString(2, customGenericException.getExecutableMethodName());
			            ps.setString(3, customGenericException.getErrorCode());
			            ps.setString(4, customGenericException.getUserid());
			            ps.setString(5, customGenericException.getClassName());
			            return ps;
			        }
			    });
		logger.info("Executing Method saveErrorLog(). Result Value: "+i);
		
	}


	
	
}
