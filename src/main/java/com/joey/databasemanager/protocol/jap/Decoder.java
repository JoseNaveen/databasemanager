package com.joey.databasemanager.protocol.jap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class Decoder {
	
	public void decode(ASN1VisibleString element,byte[] value) {
		element.setValue(new String(value));
	}
	
	public void decode(ASN1Sequence element,byte[] value) {
		ByteBuffer buffer = ByteBuffer.wrap(value);
		Field[] fields = element.getClass().getDeclaredFields();
		for (Field field : fields) {
			byte tag = buffer.get();
			Tag ann = field.getType().getAnnotation(Tag.class);
			if (Byte.toUnsignedInt(tag) == ann.value()) {
				byte lengthbyte = buffer.get();
				byte[] content = new byte[Byte.toUnsignedInt(lengthbyte)];
				buffer.get(content);
				try {
					Object obj = createInstance(field.getType());
					if (obj instanceof ASN1Element) {
						((ASN1Element) obj).decode(this,content);
					}
					field.setAccessible(true);
					field.set(element, obj);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	

	public void decode(ASN1NumericString element,byte[] value) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<value.length;i++) {
			sb.append(String.format("%02X", value[i]));
		}
		element.setValue(sb.toString());
	}
	
	public <T> T decode(ByteBuffer buffer,Class<T> cls) throws DecoderException {
		Tag ann = cls.getAnnotation(Tag.class);
		int tag = ann.value();
		int tagsize = ann.size();
		
		if (tagsize == 1) {
			byte tagbyte = buffer.get();
			if(tagbyte == (byte)tag) {
				byte lengthbyte = buffer.get();
				int lengthint = Byte.toUnsignedInt(lengthbyte);
				byte[] content = new byte[lengthint];
				buffer.get(content);
				try {
					T obj = createInstance(cls);
					if (obj instanceof ASN1Element) {
						((ASN1Element) obj).decode(this,content);
						return obj;
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				
			}
			else {
				throw new DecoderException("Invalid tag: " + tagbyte);
			}
		}
		throw new DecoderException("Unable to decode message");
		
		
		
	}
	
	private <T> T createInstance(Class<T> cls) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return cls.getDeclaredConstructor().newInstance();
	}
	
	

}
