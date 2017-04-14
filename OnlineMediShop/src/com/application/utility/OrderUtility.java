package com.application.utility;

import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderUtility {
	private static final Logger logger = LoggerFactory.getLogger(OrderUtility.class);
	public static Timestamp orderTimestamp(){
		 java.util.Date date= new java.util.Date();
		 return new Timestamp(date.getTime());
    }
	
	public static int[] convertToInt(List<Integer> integerDataList){
		int[] intArray = new int[integerDataList.size()];
	    for (int i=0; i < intArray.length; i++)
	    {
	    	intArray[i] = integerDataList.get(i).intValue();
	    }
	    return intArray;
   }

	public static String[] convertToStringArray(List<String> objectDataList){
		String[] stringArray = new String[objectDataList.size()];
	    for (int i=0; i < stringArray.length; i++)
	    {
	    	stringArray[i] = objectDataList.get(i).toString();
	    }
	    return stringArray;
   }
	
	public static String buildUpdateQuery(String[] fieldNames,String tablename,String whereClause
			,List<String> whereClauseFieldNames){
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("UPDATE "+tablename+" SET ");
		for(int i = 0; i< fieldNames.length;i++){
			if(!whereClauseFieldNames.contains(fieldNames[i])){
				strBuf.append(" ");
				strBuf.append(fieldNames[i]);
				strBuf.append(" = ?");
				strBuf.append(",");
			}
		}
		strBuf = strBuf.replace(strBuf.length()-1, strBuf.length(), "");
		strBuf.append(" WHERE ");
		strBuf.append(whereClause);
		return strBuf.toString();
	}
	
	/*public static String buildInsert(String[] fieldNames,String tablename){
		StringBuffer strBuf = new StringBuffer();
		StringBuffer strPrefixBuf = new StringBuffer();
		strBuf.append("INSERT INTO "+tablename+" ( ");
		for(int i = 0; i< fieldNames.length;i++){
				strBuf.append(" ");
				strBuf.append(fieldNames[i]);
				strBuf.append(",");
				strPrefixBuf.append("?");
		}
		strBuf = strBuf.replace(strBuf.length()-1, strBuf.length(), "");
		strBuf.append(")");
		strBuf.append(" VALUES ");
		strBuf.append("( "+strPrefixBuf.toString()+" )");
		return strBuf.toString();
	}*/
	
	public static String convertToWhereClauseString(List<String> whereClauseFieldNames){
		StringBuffer whereClause = new StringBuffer();
	    for (int i=0; i < whereClauseFieldNames.size(); i++){
	    	whereClause.append(whereClauseFieldNames.get(i).toString());
	    	whereClause.append(" = ?");
	    	whereClause.append(" and ");
	    }
	    whereClause = whereClause.replace(whereClause.lastIndexOf("and"), whereClause.length(), "");
	    return whereClause.toString();
   }
	
	public static String getIPAddress(){
		String ipAddress = "";
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress();
		} catch (Exception e) {
			logger.info("Exception Occured during getting ipAddress::"+e.getMessage());
		}
		return ipAddress;
	}
	
	public static String getOrderQueryString(List<BigInteger> orders){
		StringBuilder strBuilder = new StringBuilder();
		for(BigInteger orderId : orders){
			strBuilder.append(orderId.toString());
			strBuilder.append(",");
		}
		String orderQueryStr = strBuilder.toString();
		orderQueryStr = orderQueryStr.substring(0, orderQueryStr.length()-1);
		return orderQueryStr;
	}
}
