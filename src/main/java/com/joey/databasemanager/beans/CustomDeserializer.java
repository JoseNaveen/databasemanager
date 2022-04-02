package com.joey.databasemanager.beans;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class CustomDeserializer extends StdDeserializer<VendorSpecificTag>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 10101L;

	protected CustomDeserializer(Class<?> vc) {
		super(vc);
	}
	
	public CustomDeserializer() {
		this(null);
	}

	@Override
	public VendorSpecificTag deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		JsonNode node = p.getCodec().readTree(p);
		
		
		String classname = node.get("Java_class").asText();
		try {
			Object instance = Class.forName(classname).getDeclaredConstructor().newInstance();
			JsonNode value = node.get("value");
			Iterator<Entry<String, JsonNode>> iterator = value.fields();
			while(iterator.hasNext()) {
				var item = iterator.next();
				if (item.getValue().isValueNode()) {
					instance.getClass().getDeclaredField(item.getKey()).setAccessible(true);
					Class<?> bytearrayclass = byte[].class;
					Field f = instance.getClass().getDeclaredField(item.getKey());
					if (f.getType().isAssignableFrom(bytearrayclass)) {
						f.setAccessible(true);
						byte[] setvalue = new BigInteger(item.getValue().asText(),16).toByteArray();
						f.set(instance, setvalue);
					}
				}
				if (item.getValue().isArray()) {
					parseArray(item.getKey(),(ArrayNode)item.getValue(),instance);
				}
				else {
					parse(item.getKey(), item.getValue(), instance);
				}
			}
			return (VendorSpecificTag)instance;
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
	
	public void parseArray(String fieldName, ArrayNode a, Object o) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		Field f = o.getClass().getDeclaredField(fieldName);
		List<Object> list = new ArrayList<>();
		for(JsonNode element: a) {
			if (element.isValueNode()) {
				
			}
			if (element.isArray()) {
				
			}
			else {
				list.add(parse(element));
			}
		}
		f.setAccessible(true);
		if (f.getType().isAssignableFrom(list.getClass())) {
			f.set(o, list);
		}
		
		
	}
	
	public Object parse(JsonNode j) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String className = j.get("Java_class").asText();
		Object o = Class.forName(className).getDeclaredConstructor().newInstance();
		for(Field f: o.getClass().getFields()) {
			
		}
		
		
		
		return o;
	}
	
	
	public Object parse(String fieldName, JsonNode j, Object o) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NoSuchFieldException {
		String className = j.get("Java_class").asText();
		Object nestedinstance = Class.forName(className).getDeclaredConstructor().newInstance();
		JsonNode v = j.get("value");
		Field declaredField = o.getClass().getDeclaredField(fieldName);
		declaredField.setAccessible(true);
		declaredField.set(o, nestedinstance);
		var it = v.fields();
		while(it.hasNext()) {
			var item = it.next();
			if (item.getValue().isArray()) {
				for(JsonNode arrayitem: item.getValue()) {
					
				}
			}
			if (item.getValue().isValueNode()) {
				String t = item.getValue().asText();
				Class<?> bytearraytype = byte[].class;
				if(nestedinstance.getClass().getDeclaredField(item.getKey()).getType().isAssignableFrom(bytearraytype) ) {
					byte[] val = new BigInteger(t,16).toByteArray();
					Field declaredField2 = nestedinstance.getClass().getDeclaredField(item.getKey());
					declaredField2.setAccessible(true);
					declaredField2.set(nestedinstance, val);
				}
				Class<?> bytetype = byte.class;
				if(nestedinstance.getClass().getDeclaredField(item.getKey()).getType().isAssignableFrom(bytetype) ) {
					byte val = (byte)Integer.parseInt(t);
					Field declaredField2 = nestedinstance.getClass().getDeclaredField(item.getKey());
					declaredField2.setAccessible(true);
					declaredField2.set(nestedinstance, val);
				}
			}
		}
		
		return o;
	}
	
	

}
