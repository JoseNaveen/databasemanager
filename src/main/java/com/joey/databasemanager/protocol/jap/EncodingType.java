package com.joey.databasemanager.protocol.jap;

import java.util.function.Function;

public enum EncodingType {
	
	TYPE1(el -> {
		if (el instanceof ASN1NumericString) {
			ASN1NumericString numstr = (ASN1NumericString) el;
		}
		return null;
	});
	
	private Function<ASN1Element,byte[]> function;
	
	private EncodingType(Function<ASN1Element,byte[]> f) {
		this.function = f;
	}
	
	public byte[] apply(ASN1Element el) {
		return this.function.apply(el);
	}

}
