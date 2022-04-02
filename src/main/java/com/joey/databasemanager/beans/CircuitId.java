package com.joey.databasemanager.beans;

public class CircuitId extends Subtag {
	
	private byte subtag_type;
	
	private byte[] subtag_value;

	public byte getSubtag_Type() {
		return subtag_type;
	}

	public void setSubtag_Type(byte type) {
		this.subtag_type = type;
	}

	public byte[] getSubtag_value() {
		return subtag_value;
	}

	public void setSubtag_value(byte[] subtag_value) {
		this.subtag_value = subtag_value;
	}
	
	

}
