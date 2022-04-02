package com.joey.databasemanager.implalpha;

public class ProtocolMessage {
	
	enum MessageType {
		START_CAPTURE,
		STOP_CAPTURE
	}
	
	private MessageType messageType;
	
	private String payload;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	
	

}
