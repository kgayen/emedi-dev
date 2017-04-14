package com.application.exception;

public class CustomGenericException extends RuntimeException {
	
	private static final long serialVersionUID = 4311138296596467753L;
	private String errorCode;
	private String errorMsg;
	private String className;
	private String executableMethodName;
	private String userid;
	
	public CustomGenericException(String errorCode,String errorMsg,String className,
			String executableMethodName,String userid){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.className = className;
		this.executableMethodName = executableMethodName;
		this.userid = userid;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getExecutableMethodName() {
		return executableMethodName;
	}
	public void setExecutableMethodName(String executableMethodName) {
		this.executableMethodName = executableMethodName;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{\"error\":[{\"errorcode\" : \""+errorCode+"\",\"errorMsg\" : \""+errorMsg+"\","
				+ "\"className\" : \""+className+"\",\"executableMethodName\" : \""+executableMethodName+"\"}]}";
	}
}
