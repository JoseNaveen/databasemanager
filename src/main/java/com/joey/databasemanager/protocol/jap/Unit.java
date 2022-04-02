package com.joey.databasemanager.protocol.jap;

public enum Unit {
	
	BYTE(8),
	BIT(1);
	
	private int value;
	private Unit(int i) {
		value = i;
	}
	
	public int convert(int val) {
		return val*value;
	}

}
