package com.joey.databasemanager.implalpha;

public class TextMessage {
	
	
	private int msgType;
	private int msgLength;
	private String msgPayload;
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getMsgLength() {
		return msgLength;
	}
	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}
	public String getMsgPayload() {
		return msgPayload;
	}
	public void setMsgPayload(String msgPayload) {
		this.msgPayload = msgPayload;
	}
	
	
	

}
