package com.application.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback; 
import org.springframework.jdbc.core.ResultSetExtractor;

import com.application.dao.UserDao;
import com.application.model.User;

public class UserService extends  BaseService implements UserDao<User> {
	
	private JdbcTemplate template; 
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}

	public List<User> getDataList(Object obj) {
		logger.info("Executing Method getDataList().");
		User userQuery = (User) obj;
		final String query = "select * from user where user_id = '"+userQuery.getUser_id()+"' and user_password = '"+userQuery.getUser_password()+"'";
		logger.info("Executing Method getDataList(). Query : "+query);
		return (List<User>) template.query(query,new ResultSetExtractor<List<User>>(){  
			public List<User> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<User> list=new ArrayList<User>();  
				while(rs.next()){  
					User userObj=new User();
					userObj.setUser_id(rs.getString(1));
					userObj.setUser_name(rs.getString(3));
					userObj.setUser_role(rs.getString(4));
					userObj.setUser_email(rs.getString(5));
					userObj.setUser_create_date(String.valueOf(rs.getTimestamp(6)));
					userObj.setUser_creator(rs.getString(7));
					userObj.setUser_mobile(rs.getString(8));
					userObj.setUser_address(rs.getString(9));
					userObj.setUser_pincode(rs.getString(10));
					list.add(userObj);  
				}  
				return list;  
			}  
		});
	}

	public Boolean updateData(Object o) {
		final User user = (User)o;
		String query="update user set "+  
	    		   "user_name=?,user_role=?,user_email = ?,user_creator=?,user_mobile =?, user_address =?,user_pincode =?"+
	    		   " where user_id='"+user.getUser_id()+"'";
	    return template.execute(query,new PreparedStatementCallback<Boolean>(){  
	    public Boolean doInPreparedStatement(PreparedStatement ps)  
	            throws SQLException, DataAccessException {
	    	ps.setString(1,user.getUser_name());
	    	ps.setString(2,user.getUser_role());
	    	ps.setString(3,user.getUser_email());
	    	ps.setString(4,user.getUser_creator());
	    	ps.setString(5,user.getUser_mobile());
	    	ps.setString(6,user.getUser_address());
	    	ps.setString(7,user.getUser_pincode());
	        return ps.execute();  
	              
	    }  
	    });
	}
	
	
	public List<User> getDataListByUserId(String user_id) {
		logger.info("Executing Method getDataListByUserId().");
		final String query = "select * from user where user_id = '"+user_id+"'";
		logger.info("Executing Method getDataListByUserId(). Query : "+query);
		return (List<User>) template.query(query,new ResultSetExtractor<List<User>>(){  
			public List<User> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<User> list=new ArrayList<User>();  
				while(rs.next()){  
					User userObj=new User();
					userObj.setUser_id(rs.getString(1));
					userObj.setUser_name(rs.getString(3));
					userObj.setUser_role(rs.getString(4));
					userObj.setUser_email(rs.getString(5));
					userObj.setUser_create_date(String.valueOf(rs.getTimestamp(6)));
					userObj.setUser_mobile(rs.getString(8));
					userObj.setUser_address(rs.getString(9));
					userObj.setUser_pincode(rs.getString(10));
					list.add(userObj);  
				}  
				return list;  
			}  
		});
	}

	public Boolean insertData(Object o) {
		// TODO Auto-generated method stub
		final User user = (User)o;
		String query="insert into user "
					+"(user_id,user_password,user_name,user_role,user_email,user_creator,user_mobile,user_address,user_pincode)"
					+ "values(?,?,?,?,?,?,?,?,?)";
	    return template.execute(query,new PreparedStatementCallback<Boolean>(){  
	    public Boolean doInPreparedStatement(PreparedStatement ps)  
	            throws SQLException, DataAccessException {
	    	ps.setString(1,user.getUser_id());
	    	ps.setString(2,user.getUser_password());
	    	ps.setString(3,user.getUser_name());
	    	ps.setString(4,user.getUser_role());
	    	ps.setString(5,user.getUser_email());
	    	ps.setString(6,user.getUser_id());
	    	ps.setString(7,user.getUser_mobile());
	    	ps.setString(8,user.getUser_address());
	    	ps.setString(9,user.getUser_pincode());
	        return ps.execute();
	    }  
	    });
	}

	public int deleteData(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Boolean changePassword(Object o) {
		// TODO Auto-generated method stub
		final User user = (User)o;
		String query="update user set "+  
	    		   "user_password=? where user_id='"+user.getUser_id()+"'";
	    return template.execute(query,new PreparedStatementCallback<Boolean>(){  
	    public Boolean doInPreparedStatement(PreparedStatement ps)  
	            throws SQLException, DataAccessException {
	    	ps.setString(1,user.getUser_password());
	        return ps.execute();
	    }  
	    });
	}

	public List<User> getUserListByMobileNo(Object o) {
		logger.info("Executing Method getUserListByMobileNo().");
		// TODO Auto-generated method stub
		final User user = (User) o;
		final String query = "select * from user where user_mobile = '"+user.getUser_mobile()+"'";
		logger.info("Executing Method getUserListByMobileNo(). Query : "+query);
		return (List<User>) template.query(query,new ResultSetExtractor<List<User>>(){  
			public List<User> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<User> list=new ArrayList<User>();  
				while(rs.next()){  
					User userObj=new User();
					userObj.setUser_id(rs.getString(1));
					userObj.setUser_name(rs.getString(3));
					userObj.setUser_role(rs.getString(4));
					userObj.setUser_email(rs.getString(5));
					userObj.setUser_create_date(String.valueOf(rs.getTimestamp(6)));
					userObj.setUser_mobile(rs.getString(8));
					userObj.setUser_address(rs.getString(9));
					userObj.setUser_pincode(rs.getString(10));
					list.add(userObj);  
				}  
				return list;  
			}  
		});
	}	
}
