package com.joey.databasemanager.exceptions;

public class ApiError {
	
	
	private String errorMessage;
	
	
	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public ApiError(String message) {
		this.errorMessage = message;
	}

}
