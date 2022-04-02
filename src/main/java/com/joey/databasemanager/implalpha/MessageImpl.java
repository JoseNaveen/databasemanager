package com.joey.databasemanager.implalpha;

public class MessageImpl extends Message{
	
	final String message;
	String command;
	
	public MessageImpl(String message) {
		this.message = message;
	}
	
	public MessageImpl(String message,String command) {
		this.message = message;
		this.command = command;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	@Override 
	public String getCommand() {
		return this.command;
	}

}
