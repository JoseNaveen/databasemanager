package com.joey.databasemanager.protocol.jap;

import java.nio.ByteBuffer;

public abstract class ASN1Element {
	
	public abstract void encode(Encoder encoder);
	
	public abstract void decode(Decoder decoder,byte[] value);

}
