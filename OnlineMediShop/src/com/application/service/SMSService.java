package com.application.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import com.application.dao.SMSDao;
import com.application.model.SMSModel;
import com.application.utility.SpringPropertiesUtil;
import com.opensymphony.xwork2.util.TextUtils;

//@PropertySource({"classpath:SMS.properties"})
public class SMSService extends  BaseService implements SMSDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SMSService.class);
	
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}

	public void sendSMSJSON(SMSModel smsModel) throws IOException,JSONException {
        try{
        	StringBuilder parameters = new StringBuilder();
        	parameters.append(SpringPropertiesUtil.getProperty("URLOPOST_serviceURL"));
        	parameters.append("&");
        	parameters.append("to=" +  TextUtils.join(",", smsModel.getSenderList()));
        	parameters.append("&");
        	parameters.append("sender=" +SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
        	parameters.append("&");
        	parameters.append("message=" +smsModel.getMessage());
        	
        	
        	URL url = new URL(parameters.toString());
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setDoInput (true);
            con.setReadTimeout(2000);
            con.setUseCaches (false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter wr = new PrintWriter(con.getOutputStream(), true);
            wr.print(parameters);
            logger.info("parameters : "+parameters.toString());
            smsModel.setRequestString(parameters.toString());
            wr.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine1 = br.readLine();
            logger.info("SMS Response message not processed : "+inputLine1);
            br.close(); 

            if(inputLine1.contains("ES1001") || inputLine1.contains("ES1002") || inputLine1.contains("ES1003") || inputLine1.contains("ES1004") || inputLine1.contains("ES1005") || inputLine1.contains("ES1006") || inputLine1.contains("ES1007") || inputLine1.contains("ES1008") || inputLine1.contains("ES1009") || inputLine1.contains("ES1010") || inputLine1.contains("ES1011") || inputLine1.contains("ES1012") || inputLine1.contains("ES1013") || inputLine1.contains("ES1014") || inputLine1.contains("ES1015") || inputLine1.contains("ES1016") || inputLine1.contains("ES1017") || inputLine1.contains("ES1018") || inputLine1.contains("Your Request has been not proceed...!") || inputLine1.contains("You have Exceeded your SMS Limit") || inputLine1.contains("Account is Expire")){
            	logger.info("Message not processed : "+inputLine1);
            	smsModel.setSmsErrorCode(inputLine1);
            	smsModel.setResponseMessage(inputLine1);
            } 
            else{
                JSONObject jsonRespObject = null;
                try{
                    jsonRespObject = new JSONObject(inputLine1);
                    //String result = jsonRespObject.getString("result");
                    //String message = jsonRespObject.getString("message");
                    String errorcode = jsonRespObject.getString("Errorcode");
                    smsModel.setSmsErrorCode(errorcode);
                    smsModel.setResponseMessage(inputLine1);
                    logger.info("Message has been sent to "+smsModel.getTo());
                }
                catch (JSONException jsonExcep){
                	System.out.println(jsonExcep);
                	smsModel.setResponseMessage(jsonExcep.getMessage());
                	throw jsonExcep;
                }
            }
        }
        catch (IOException ioExcep){
        	smsModel.setResponseMessage(ioExcep.getMessage());
        	System.out.println(ioExcep);
        	throw ioExcep;
        }
        //saveSMSTransaction(smsModel);
	}
	
	public void sendSMSJavaPOST(SMSModel smsModel) throws IOException{
		try{
			String postData="";
			String retval = "";
			//give all Parameters In String
			String Username =SpringPropertiesUtil.getProperty("userID");
			String Password = SpringPropertiesUtil.getProperty("password");
			String MobileNo = smsModel.getTo();
			String Message = smsModel.getMessage();
			String SenderID = SpringPropertiesUtil.getProperty("senderid");
			postData += "username=" + Username + "&password=" + Password + "&to=" +
			MobileNo +"&sender=" + SenderID + "&message=" + Message;
			URL url = new URL("http://instant.kapsystem.com/web2sms.php?");
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("POST");
			urlconnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			urlconnection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
			out.write(postData);
			out.close();
			BufferedReader in = new BufferedReader( new
			InputStreamReader(urlconnection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
			retval += decodedString;
			}
			in.close();
			logger.info("SMS Response Message: "+retval);
		}
		catch(IOException ioExcep){
			throw ioExcep;
		}
	}
	
	public void sendSMSJavaURLPOST(SMSModel smsModel) throws IOException{
		try{
			String postData="";
			String retval = "";
			StringBuilder parameters = new StringBuilder(SpringPropertiesUtil.getProperty("URLOPOST_serviceURL"));
            parameters.append("username=" + SpringPropertiesUtil.getProperty("URLOPOST_userID"));
            parameters.append("&");
            parameters.append("pass=" + SpringPropertiesUtil.getProperty("URLOPOST_password"));
            parameters.append("&");
            parameters.append("senderid=" + SpringPropertiesUtil.getProperty("URLOPOST_senderid"));
            parameters.append("&");
            parameters.append("dest_mobileno=" + smsModel.getTo());
            parameters.append("&");
            parameters.append("tempid=" + smsModel.getTemplateId());
            parameters.append("&");
            StringBuilder messageVariable = new StringBuilder();
            for(int i = 0; i<smsModel.getSmsVariableProperties().size();i++){
            	messageVariable.append(smsModel.getSmsVariableProperties().get(i)+"|");
            	parameters.append("F"+String.valueOf(i+1)+"="+smsModel.getSmsVariableProperties().get(i)+"&");
            }
            parameters.append("response=" + SpringPropertiesUtil.getProperty("URLOPOST_responseType"));
			final String smsSendingURL = parameters.toString();
			logger.info("SMS Sender URL: "+smsSendingURL);
			parameters.replace(parameters.indexOf("username="), parameters.indexOf("&pass"), "XX");
            parameters.replace(parameters.indexOf("pass="), parameters.indexOf("&senderid"), "XX");
            smsModel.setRequestString(parameters.toString());
			smsModel.setSmsResponseFlag(SpringPropertiesUtil.getProperty("URLOPOST_responseType"));
			smsModel.setMessage(messageVariable.toString());
			
			URL url = new URL(smsSendingURL);
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("GET");
			urlconnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			urlconnection.setDoOutput(true);
			urlconnection.setUseCaches (false);
			urlconnection.setReadTimeout(3000);
			OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
			out.write(postData);
			out.close();
			BufferedReader in = new BufferedReader( new
			InputStreamReader(urlconnection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				retval += decodedString;
			}
			in.close();
			smsModel.setResponseMessage(retval);
			if(retval.startsWith("ES")){
				smsModel.setSmsErrorCode(retval.substring(0, 6));
			}
			logger.info("SMS Response Message: "+retval);
		}
		catch(IOException ioExcep){
			smsModel.setResponseMessage(ioExcep.getMessage());
			smsModel.setSmsErrorCode(SpringPropertiesUtil.getProperty("SMS_IOEXCEPTION"));
			saveSMSTransaction(smsModel);
			throw ioExcep;
		}
		saveSMSTransaction(smsModel);
	}
	
	public void saveSMSTransaction(final SMSModel smsModel){
		logger.info("Executing Method saveSMSTransaction().");
		final String query="INSERT INTO medishop.sms_details"
				+"(Message,Recipient,SMSTemplateId,SmsResponseFlag,SmsErrorCode,ResponseMessage,SMSRequestString,ResponseJSON)"
				+ " VALUES"
				+ "(?,?,?,?,?,?,?,?)";
		logger.info("Executing Method saveSMSTransaction(). Query : "+query);
		try{
			template.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(query);
			            ps.setString(1, smsModel.getMessage());
			            ps.setString(2, smsModel.getTo());
			            ps.setString(3, smsModel.getTemplateId());
			            ps.setString(4, smsModel.getSmsResponseFlag());
			            ps.setString(5, smsModel.getSmsErrorCode());
			            ps.setString(6, smsModel.getResponseMessage());
			            ps.setString(7, smsModel.getRequestString());
			            ps.setString(8, smsModel.getResponseJSONMessage());
			            return ps;
			        }
			    });
		}
		catch(Exception excep){
			logger.info("Exception occured due to insert SMS details saveSMSTransaction() : "+excep.getMessage());
		}
	}
}
