package com.joey.databasemanager.protocol.jap;

@Length(size = 1,unit = Unit.BYTE)
@Tag(value = 12,
size=1,
unit = Unit.BYTE)
public class Name extends ASN1VisibleString {
	
	
	private String value;
	
	
	public Name() {
		
	}
	public Name(String v) {
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String str) {
		this.value = str;
	}

}