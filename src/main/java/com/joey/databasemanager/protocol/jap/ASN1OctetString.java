package com.joey.databasemanager.protocol.jap;

public abstract class ASN1OctetString extends ASN1Element {

	public abstract byte[] getValue();
	
	public abstract void setValue(byte[] val);

}
