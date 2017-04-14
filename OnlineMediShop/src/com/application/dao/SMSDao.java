package com.application.dao;

import java.io.IOException;

import org.json.JSONException;

import com.application.model.SMSModel;

public interface SMSDao {
	
	public void sendSMSJSON(SMSModel smsModel) throws IOException,JSONException;
	public void sendSMSJavaPOST(SMSModel smsModel) throws IOException;
	public void sendSMSJavaURLPOST(SMSModel smsModel) throws IOException;
	public void saveSMSTransaction(SMSModel smsModel);

}
