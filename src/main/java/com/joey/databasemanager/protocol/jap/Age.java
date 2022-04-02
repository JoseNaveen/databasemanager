package com.joey.databasemanager.protocol.jap;


@Tag(value = 16)
@Length(size = 2)
public class Age extends ASN1Integer {
	
	
	private final int value;
	
	public Age(int v) {
		this.value = v;
	}
	
	@Override
	public int getValue() {
		return value;
	}

}
