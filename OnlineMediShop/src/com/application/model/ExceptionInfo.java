package com.application.model;

public class ExceptionInfo {
	
	private String errorCode;
	private String errorMsg;
	private String className;
	private String executableMethodName;
	
	public ExceptionInfo(String errorCode,String errorMsg,String className,
			String executableMethodName){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.className = className;
		this.executableMethodName = executableMethodName;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{\"errorcode\" = \""+errorCode+"\",\"errorMsg\" = \""+errorMsg+"\","
				+ "\"className\" = \""+className+"\",\"executableMethodName\" = \""+executableMethodName+"\"}";
	}
}