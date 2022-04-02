package com.joey.databasemanager.protocol.jap;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.joey.io.BitBuffer;
import com.joey.io.BitBuffer.Mode;

public class Encoder {
	
	public byte[] encode1(ASN1Integer element) throws EncodingException {
		
		
		int tagvalue = 16;
		int tagsize = 1;
		Unit tagunit = Unit.BYTE;
		BitBuffer buffer = new BitBuffer(3,Mode.MODE2);
		
		int lengthsize = 1;
		Unit lengthunit = Unit.BYTE;
		buffer.addInteger(tagvalue, tagunit.convert(tagsize));
		int length = (Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue()) % 8) == 0 ? 
				(Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue())) / 8 : 1 + (Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue())) / 8;
		buffer.addInteger(length, lengthunit.convert(lengthsize));
		buffer.addInteger(element.getValue(), length * 8);
		
		return buffer.array();
	}
	
	public byte[] encode(ASN1Integer element) throws EncodingException {
		Tag tagann = null;
		if (element.getClass().getDeclaredAnnotation(Tag.class) !=null) {
			tagann = element.getClass().getDeclaredAnnotation(Tag.class);
		}
		
		int tagvalue = tagann.value();
		int tagsize = tagann.size();
		Unit tagunit = tagann.unit();
		BitBuffer buffer = new BitBuffer(3,Mode.MODE2);
		Length lengthann = null;
		if (element.getClass().getDeclaredAnnotation(Length.class)!=null) {
			lengthann = element.getClass().getDeclaredAnnotation(Length.class);
		}
		int lengthsize = lengthann.size();
		Unit lengthunit = lengthann.unit();
		buffer.addInteger(tagvalue, tagunit.convert(tagsize));
		int length = (Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue()) % 8) == 0 ? 
				(Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue())) / 8 : 1 + (Integer.SIZE - Integer.numberOfLeadingZeros(element.getValue())) / 8;
		buffer.addInteger(length, lengthunit.convert(lengthsize));
		buffer.addInteger(element.getValue(), length * 8);
		
		return buffer.array();
	}
	
	public byte[] encode(ASN1VisibleString element) {
		
		int tag;
		int tagsize;
		int lengthsize;
		if(element.getClass().isAnnotationPresent(Tag.class)) {
			Tag ann = element.getClass().getAnnotation(Tag.class);
			tag = ann.value();
			tagsize = ann.size();
		}
		else {
			tag = 1;
			tagsize = 1;
		}
		if(element.getClass().isAnnotationPresent(Length.class)) {
			Length ann = element.getClass().getAnnotation(Length.class);
			lengthsize = ann.size();
		}
		else {
			lengthsize = 1;
		}
		ByteBuffer buf = ByteBuffer.allocate(tagsize + lengthsize + element.getValue().getBytes().length);
		if(tagsize == 1) {
			System.out.println("tag: " + tag);
			buf.put((byte)tag);
		}
		if (lengthsize == 1) {
			buf.put((byte)element.getValue().getBytes().length);
		}
		buf.put(element.getValue().getBytes());
		System.out.println("position: " + buf.position());
		byte[] newarray = new byte[buf.position()];
		buf.flip();
		buf.get(newarray);
		return newarray;
	}

	public byte[] encode(ASN1NumericString element) {
		int tag;
		int tagsize;
		int lengthsize;
		if(element.getClass().isAnnotationPresent(Tag.class)) {
			Tag ann = element.getClass().getAnnotation(Tag.class);
			tag = ann.value();
			tagsize = ann.size();
		}
		else {
			tag = 1;
			tagsize = 1;
		}
		if(element.getClass().isAnnotationPresent(Length.class)) {
			Length ann = element.getClass().getAnnotation(Length.class);
			lengthsize = ann.size();
		}
		else {
			lengthsize = 1;
		}
		int numberlength = element.getValue().length();
		if (numberlength % 2 == 0) {
			numberlength = numberlength / 2;
		}
		else {
			numberlength++;
			numberlength = numberlength / 2;
		}
		ByteBuffer buf = ByteBuffer.allocate(tagsize + lengthsize + numberlength);
		if(tagsize == 1) {
			System.out.println("tag: " + tag);
			buf.put((byte)tag);
		}
		if (lengthsize == 1) {
			buf.put((byte)numberlength);
		}
		String numberstring = element.getValue();
		for (int i = 0,j=0; i<numberlength; i++,j=j+2) {
			/**
			 * Iteration: 1
			 * j = 0
			 * 5 > 0 + 2, 5 > 2
			 * 0,1
			 * Iteration: 2
			 * j = 2
			 * 5 > 4
			 * 2,3
			 * Iteration: 3
			 * j = 4
			 * 5 > 6
			 * 
			 */
			if (numberstring.length()>=j+2) {
				char first = element.getValue().charAt(j);
				char second = element.getValue().charAt(j+1);
				int val = Character.getNumericValue(first) << 4 | Character.getNumericValue(second);
				buf.put(new byte[] {(byte)val});
			}
			else {
				char first = element.getValue().charAt(j);
				int val = (Character.getNumericValue(first) << 4) | (15<<0);
				buf.put(new byte[] {(byte)val});
			}
			
		}
		
		byte[] newarray = new byte[buf.position()];
		buf.flip();
		buf.get(newarray);
		return newarray;
		
	}
	
	public byte[] encode(ASN1Sequence element) {
		
		ByteBuffer buffer = ByteBuffer.allocate(65535);
		
		if(element.getClass().isAnnotationPresent(JsonPropertyOrder.class)) {
			var propertyOrder = element.getClass().getAnnotation(JsonPropertyOrder.class);
			String[] propertyList = propertyOrder.value();
			int tempValue = 0;
			byte tempByte = (byte)0;
			for(String property: propertyList) {
				Class<?> fieldType;
				try {
					fieldType = element.getClass().getField(property).getType();
					if (fieldType.isAnnotationPresent(Tag.class)) {
						
					}
					else {
						Field field = element.getClass().getField(property);
						field.setAccessible(true);
						if(field.getType().isAssignableFrom(ASN1Integer.class)) {
							Encoding encoding = field.getType().getAnnotation(Encoding.class);
							int size = encoding.size();
							if (size % 8 != 0) {
								if (size < 8) {
									
								}
							}
						}
						
					}
				} catch (NoSuchFieldException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		}
		int tag;
		if(element.getClass().isAnnotationPresent(Tag.class) ) {
			Tag ann = element.getClass().getAnnotation(Tag.class);
			tag = ann.value();
			int size = ann.size();
			if (size == 1) {
				System.out.println("tag: " + tag);
				buffer.put((byte)tag);
			}
		}
		int size = 1;
		if (element.getClass().isAnnotationPresent(Length.class)) {
			Length ann = element.getClass().getAnnotation(Length.class);
			size = ann.size();
		}
		List<ASN1Element> l = element.getValue();
		List<byte[]> bytelist = new ArrayList<>(l.size());
		for(ASN1Element e: l) {                
			
			if (e instanceof ASN1VisibleString) {
				ASN1VisibleString asvstr = (ASN1VisibleString)e;
				
				byte[] b = encode(asvstr);
				bytelist.add(b);
			}
			if (e instanceof ASN1NumericString) {
				ASN1NumericString asvstr = (ASN1NumericString)e;
				
				byte[] b = encode(asvstr);
				bytelist.add(b);
			}
		}
		int length = bytelist.stream().map(item -> item.length).reduce(0, Integer::sum);
		if(size == 1) {            
			buffer.put((byte)length);
		}
		bytelist.stream().forEach(item-> {
			buffer.put(item);
		});
		byte[] newarray = new byte[buffer.position()];
		buffer.flip();
		buffer.get(newarray);
		return newarray;		
	}
}
