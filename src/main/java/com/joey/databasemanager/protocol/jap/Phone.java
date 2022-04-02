package com.joey.databasemanager.protocol.jap;


@Length(size = 1, unit = Unit.BYTE)
@Tag(value = 13,
	size=1,
	unit = Unit.BYTE)
public class Phone extends ASN1NumericString {
	
	private String value;
	
	public Phone(String v) {
		this.value = v;
	}
	
	public Phone() {
		
	}
	
	public String getValue() {
		return value;
	}	
	
	public void setValue(String s) {
		this.value = s;
	}
}