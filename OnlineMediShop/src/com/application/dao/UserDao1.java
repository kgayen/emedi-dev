package com.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.application.model.User;

public abstract class UserDao1<T> implements UserDao<T> {
	
	private JdbcTemplate template;  
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getDataList(Object obj) {
		// TODO Auto-generated method stub
		User userQuery = (User) obj;
		final String query = "select * from user where user_id = '"+userQuery.getUser_id()+"' and user_password = '"+userQuery.getUser_password()+"'";
		return (List<T>) template.query(query,new ResultSetExtractor<List<User>>(){  
			public List<User> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<User> list=new ArrayList<User>();  
				while(rs.next()){  
					User userObj=new User();
					userObj.setUser_id(rs.getString(1));
					userObj.setUser_name(rs.getString(3));
					userObj.setUser_role(rs.getString(4));
					userObj.setUser_email(rs.getString(6));
					userObj.setUser_create_date(String.valueOf(rs.getTimestamp(7)));
					list.add(userObj);  
				}  
				return list;  
			}  
		});
	}
}
