package com.joey.databasemanager.protocol.jap;

import java.nio.ByteBuffer;
import java.util.List;

public abstract class ASN1Sequence extends ASN1Element {
	public void encode(Encoder encoder) {
		encoder.encode(this);
	}
	
	public void decode(Decoder decoder,byte[] buffer) {
		decoder.decode(this,buffer);
	}
	
	public abstract List<ASN1Element> getValue();
	
}