package com.application.dao;

import java.util.List;

public interface UserDao<User> {
	
	public Boolean updateData(Object o);
	public Boolean insertData(Object o);
	public List<User> getDataList(Object o);
	public int deleteData(Object o);
	public Boolean changePassword(Object o);
	public List<User> getUserListByMobileNo(Object o);
}
