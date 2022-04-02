package com.joey.io;

import com.joey.databasemanager.protocol.jap.DecoderException;

public abstract class Strategy {
	public abstract void addInteger(int val,int digits);
	public abstract int getInteger(int digits) throws DecoderException;
	//public abstract void addLong(long val,int digits);
	
	//public abstract void addByteArray(byte[] array);
	//public abstract byte[] getByteArray(int length);
}