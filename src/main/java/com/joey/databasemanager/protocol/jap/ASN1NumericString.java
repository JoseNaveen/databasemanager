package com.joey.databasemanager.protocol.jap;

import java.nio.ByteBuffer;

public abstract class ASN1NumericString extends ASN1Element {
	public void encode(Encoder encoder) {
		encoder.encode(this);
	}
	
	public void decode(Decoder decoder,byte[] value) {
		decoder.decode(this,value);
	}
	public abstract String getValue();
	public abstract void setValue(String str);

}
