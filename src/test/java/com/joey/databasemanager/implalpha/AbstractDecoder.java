package com.joey.databasemanager.implalpha;

import com.joey.databasemanager.implalpha.ProtocolTests.DecodeContext;
import com.joey.databasemanager.protocol.jap.DecoderException;
import com.joey.io.BitBuffer;

public abstract class AbstractDecoder<T> {
	
	public abstract T decode(BitBuffer b, DecodeContext ctxt, Class<?> cl) throws DecoderException ;
	public abstract T decodeValue(BitBuffer buffer, DecodeContext ctxt, Class<?> cl) throws DecoderException;
	
}