package com.application.dao;

import com.application.exception.CustomGenericException;

public interface LogActivityDao {
	public void saveLogActivity(final String userid,final String eventMessage,final String eventType,final String eventClassName,
			final String eventFunctionName);
	public void saveErrorLog(final CustomGenericException customGenericException);
}
