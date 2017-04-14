package com.application.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.application.model.SMSModel;
import com.application.utility.SpringPropertiesUtil;

//@PropertySource({"classpath:SMS.properties"})
public class SendSMSService {
	private static final Logger logger = LoggerFactory.getLogger(SendSMSService.class);
	
	@Autowired
	private JSONObject jsonObj;
	@Autowired
	private SMSService smsService;
	@Autowired
	private SMSModel smsModel;
	
	 public void addSslCertificate() throws NoSuchAlgorithmException, KeyManagementException{
		TrustManager[] trustAllCerts = new TrustManager[] {
				   new X509TrustManager() {
				      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				        return null;
				      }
				      public void checkClientTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
				      }
				      public void checkServerTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						}
				   }
				};
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
				    public boolean verify(String hostname, SSLSession session) {
						return false;
					}
				};
				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				/*
				 * end of the fix
				 */
	}
	
     //  declaring class variable
     static String api_key ;
     static String sender_id;
     static String api_url;
     static String start;
     static String method;
     String time;
	 String mob_no;
	 String message;
	 String unicode;
	 String dlr_url;
	 String type;
	 // function to set sender id
	 public static  void setsender_id(String sid){      
		sender_id=sid;
		return;
	 }
       // function to set api_key key  
	 public static  void setapi_key(String apk){ 	
			// checking for valid working key
		api_key=apk;
		return;
	}
     
     // function to set method
	 public static  void setmethod(String mt){ 	
			// checking for valid working key
	 method=mt;
		return;
	}
						
	    
	    // function to set Api url
	 public static  void setapi_url(String ap){   
			//checking for valid url format
		String check= ap;
		String str=check.substring(0,7);
		String t="http://";
		String s="https:/";
		String st="https://";
		if(str.equals(t))
			 { 
			 	start=t;
				api_url=check.substring(7);
			}
		else if(check.substring(0,8).equals(st))
		{ 
		start=st;
	    api_url=check.substring(8);
		}
		else if(str.equals(s))
			{ 
			start=st;
	        api_url=check.substring(7);
			}
	 	else
		   { 
			 start=t;
	         api_url=ap;
			}
	}
        //function to set parameter import java.net.URLEncoder;
	 /*public  void setparams(String ap,String mt,String apk,String sd){ 
		setapi_key(apk);
		    //System.out.println(wk);
	    setsender_id(sd);
	    setapi_url(ap);
	    setmethod(mt);
	 }*/
	// @PostConstruct
	 public static void setparams() throws Exception{
			setapi_key(SpringPropertiesUtil.getProperty("URLJOSNGET_JSONKEY"));
			    //System.out.println(wk);
		    setsender_id(SpringPropertiesUtil.getProperty("URLJOSNGET_senderid"));
		    setapi_url(SpringPropertiesUtil.getProperty("URL_JOSNGET"));
		    setmethod(SpringPropertiesUtil.getProperty("JSON_METHOD"));
		    logger.info("api_key : "+api_key);
		    logger.info("sender_id : "+sender_id);
	 }
		/*function to send sms 
		  @ Simple message : last two field are set to null
		  @ Unicode message :set unicode parameter to one
		  @ Scheduled message : give time in 'ddmmyyyyhhmm' format
		*/
	 public String process_sms(String mob_no,String message,String dlr_url,String unicode,String time) throws IOException, KeyManagementException, NoSuchAlgorithmException{   	
		  addSslCertificate();
			message=URLEncoder.encode(message, "UTF-8");			
	 	if (unicode==null)
			 unicode="0";
			unicode="&unicode="+unicode;
		if (time==null)
			 time="";
			else time="&time="+URLEncoder.encode(time, "UTF-8");
	    URL url = new URL(""+start+api_url+"/api/v3/?method="+method+"&api_key="+api_key+"&sender="+sender_id+"&to="+mob_no+"&message="+message+unicode+time+"&dlr_url="+dlr_url+"&format=JSON" );
	    
	    System.out.println("url look like " + url );
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestMethod("POST");
	    con.setDoOutput(true);
	    con.getOutputStream();
	    con.getInputStream();
	    BufferedReader rd;
	    String line;
	    String result = "";
	    rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	   while ((line = rd.readLine()) != null)
	    {
	       result += line;
	    }
	    rd.close(); 
	    System.out.println("Result is" + result);
		return result;
		
		
	}
	  
	  /*function to send sms 
	  @ Simple message : last two field are set to null
	  @ Unicode message :set unicode parameter to one
	  @ JSON message :json message
	  @ Scheduled message : give time in 'ddmmyyyyhhmm' format
	*/
	 public void process_sms(SMSModel smsModel) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException, Exception{   	
		 	setparams();
		 	addSslCertificate();
		  	
	        StringBuilder parameters = new StringBuilder();
	    	parameters.append(""+start+api_url+"/api/v3/?");
			parameters.append("method=" +  method);
			parameters.append("&");
			parameters.append("api_key=" +api_key);
			parameters.append("&");
			parameters.append("format=JSON");
			parameters.append("&");
			parameters.append("json="+URLEncoder.encode(smsModel.getMessage(), "UTF-8"));
			
			if(parameters.toString().length()>2048){
				smsModel.setRequestString(parameters.toString().substring(0, 2048));
			}
			else{
				smsModel.setRequestString(parameters.toString());
			}
			logger.info("Request URL : "+parameters.toString());
			URL url = new URL(parameters.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(3000);
			con.setUseCaches (false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent","Mozilla/5.0");
			con.setRequestProperty("Accept","application/json");
			logger.info("Response Code : "+con.getResponseCode());
			InputStream in = new BufferedInputStream(con.getInputStream());
			BufferedReader rd;
			String line;
			String result = "";
			rd = new BufferedReader(new InputStreamReader(in));
			while ((line = rd.readLine()) != null){
			   result += line;
			}
			rd.close();
			logger.info("JSON Response from kapsystem : "+result);
			if(smsModel.getMessage().length()>2048){
				smsModel.setMessage(smsModel.getMessage().substring(0, 2048));
			}
			try{
				jsonObj = new JSONObject(result);
				if(result.length()>2048){
					result = result.substring(0, 2048);
				}
				smsModel.setResponseJSONMessage(result);
			    smsModel.setSmsErrorCode(jsonObj.getString("status"));
			    smsModel.setResponseMessage(jsonObj.getString("message"));
			}
			catch (JSONException jsonExcep){
				smsModel.setResponseMessage(jsonExcep.getMessage());
				throw jsonExcep;
			}
	        smsService.saveSMSTransaction(smsModel);
	        if(!smsModel.getSmsErrorCode().equalsIgnoreCase("OK")){
	        	throw new Exception(smsModel.getResponseJSONMessage());
	        }
	}
			//function for checking message delivery status
     public String messagedelivery_status(String mid) throws Exception
		{
			URL url = new URL("http://"+api_url+"/api/v3/?method="+method+".groupstatus&api_key="+api_key+"&groupid="+mid+"&format=json");
//				System.out.println("url look like " + url );		 
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
		    con.getOutputStream();
			con.getInputStream();
			BufferedReader rd;
			String line;
	        String result = "";
	        rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        while ((line = rd.readLine()) != null)
	          {
	            result += line;
	          }
	        rd.close(); 
//		        System.out.println("Result is" + result);
	        return result;
        }
        
  		//function for checking group delivery status
     public String groupdelivery_status(String gid) throws Exception
		{
			URL url = new URL("http://"+api_url+"/api/v3/?method="+method+".groupstatus&api_key="+api_key+"&groupid="+gid+"&format=xml");
//				System.out.println("url look like " + url );		
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.getOutputStream();
			con.getInputStream();
			BufferedReader rd;
			String line;
	        String result = "";
	        rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = rd.readLine()) != null)
             {
	            result += line;
//					System.out.println("Result is" + result);
             }
	        rd.close(); 
//		        System.out.println("Result is" + result);
	        return result;  
        }
     
     public  void unicode_sms(String mob_no,String message,String dlr_url,String unicode) throws IOException, KeyManagementException, NoSuchAlgorithmException
		{
    	 process_sms(mob_no, message, dlr_url, unicode, time=null);  				
								
		}

     
     public  void send_sms(String mob_no,String message,String dlr_url) throws IOException, KeyManagementException, NoSuchAlgorithmException
		{
    	 process_sms(mob_no, message, dlr_url, unicode=null, time=null);  				
								
		}
     public  void send_sms(SMSModel smsModel) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException, Exception
		{
    	 process_sms(smsModel);  				
								
		}
     
     public  void schedule_sms(String mob_no,String message,String dlr_url,String unicode,String time) throws IOException, KeyManagementException, NoSuchAlgorithmException
		{
    	 process_sms(mob_no, message, dlr_url, unicode, time);  				
								
		}
     
     public  void schedule_sms(String mob_no,String message,String dlr_url,String time) throws IOException, KeyManagementException, NoSuchAlgorithmException
		{
    	 process_sms(mob_no, message, dlr_url, unicode=null, time);  				
								
		}
    @SuppressWarnings("unchecked")
	public void processSMS(List<org.json.simple.JSONObject> smsList,Integer unicode,Integer flash) throws Exception{
		org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
		smsModel = new SMSModel();
		jsonObject.put("unicode", unicode);
		jsonObject.put("flash",flash);
		jsonObject.put("sms", smsList);
		logger.info("SMS JSON String : "+jsonObject.toJSONString());
		smsModel.setMessage(jsonObject.toJSONString().replaceAll("\"", "\\\""));
		try{
			process_sms(smsModel); 
		}
		catch(Exception e){
			throw e;
		}
     }
}
