package com.joey.databasemanager.implalpha;

import java.lang.reflect.InvocationTargetException;

import com.joey.databasemanager.implalpha.ProtocolTests.DecodeContext;
import com.joey.databasemanager.protocol.jap.ASN1Integer;
import com.joey.databasemanager.protocol.jap.DecoderException;
import com.joey.databasemanager.protocol.jap.Length;
import com.joey.databasemanager.protocol.jap.Tag;
import com.joey.databasemanager.protocol.jap.Unit;
import com.joey.io.BitBuffer;

public class ASN1IntegerDecoder extends AbstractDecoder<ASN1Integer> {
	
	public ASN1Integer decode(BitBuffer buffer,DecodeContext ctxt, Class<?> cls) throws DecoderException {
		Tag tag = null;
		if(cls.getDeclaredAnnotation(Tag.class)!=null) {
			tag = cls.getDeclaredAnnotation(Tag.class);
		}
		
		int tagvalue = tag.value();
		int tagsize = tag.size();
		Unit tagsizeunit = tag.unit();
		int decodedtag = buffer.getInteger(tagsizeunit.convert(tagsize));
		if (tagvalue!=decodedtag) {
			throw new DecoderException("Incorrect Tag");
		}
		Length lengthann = null;
		if (cls.getDeclaredAnnotation(Length.class)!=null) {
			lengthann = cls.getDeclaredAnnotation(Length.class);
		}
		int lengthsize = lengthann.size();
		Unit lengthunit = lengthann.unit();
		int lengthvalue = buffer.getInteger(lengthunit.convert(lengthsize));
		int value = buffer.getInteger(lengthvalue);
		ASN1Integer ret = null;
		try {
			ret = (ASN1Integer) cls.getDeclaredConstructor(int.class).newInstance(value);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new DecoderException("Decode failure");
		}
		return ret;
	}
	
	public ASN1Integer decodeValue(BitBuffer buffer,DecodeContext ctxt, Class<?> cls) throws DecoderException {
		Length lengthann = null;
		if (cls.getDeclaredAnnotation(Length.class)!=null) {
			lengthann = cls.getDeclaredAnnotation(Length.class);
		}
		int lengthsize = lengthann.size();
		Unit lengthunit = lengthann.unit();
		int lengthvalue = buffer.getInteger(lengthunit.convert(lengthsize));
		int value = buffer.getInteger(lengthvalue);
		ASN1Integer ret = null;
		try {
			ret = (ASN1Integer) cls.getDeclaredConstructor(int.class).newInstance(value);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new DecoderException("Decode failure");
		}
		return ret;
	}
}