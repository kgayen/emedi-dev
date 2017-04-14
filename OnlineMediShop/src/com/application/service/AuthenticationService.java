package com.application.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.application.dao.AuthenticationDao;

public class AuthenticationService extends  BaseService implements AuthenticationDao {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	public String retrivePassword(String mobileNo) {
		logger.info("Executing Method retrivePassword().");
		final String query = "select * from medishop.user where user_mobile = '"+mobileNo+"'";
		logger.info("Executing Method retrivePassword(). Query : "+query);
		return (String) template.query(query,new ResultSetExtractor<String>(){  
			public String extractData(ResultSet rs) throws SQLException,  
				DataAccessException {
				String retrivePassword="";
				if(rs.next()){
					retrivePassword = rs.getString("user_password");
				}  
				return retrivePassword;  
			}  
		});
	}
}
