package com.joey.databasemanager.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public class JsonParseTest {
	public void test1() throws Exception {
		String json = """
				{
					"key1": "value1",
					"key2": "value2",
					"key3": {
							"key4": "value4"
							}
				}
				""";
		
		JsonNode node = new ObjectMapper().readTree(json);
		System.out.println(node.get("key1"));
		System.out.println(node.get("key1").isValueNode());
		System.out.println(node.get("key3").isValueNode());
	}
	public static void main(String[] args) throws Exception {
		String str = """
					{
						"Java_class": "com.joey.databasemanager.beans.VendorSpecificTag",
						"value" : {
							"tag_type": "0101",
							"vendorid": "1E4FCA284",
							"subtags": [{
										"Java_class": "com.joey.databasemanager.beans.CircuitId",
										"value": {
											"type": "04",
											"subtag_value": "0438EFBDAC"
												}
									   }]
							}
					}
				""";
		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
				  .allowIfSubType("com.joey.databasemanager.beans")
				  .allowIfSubType("java.util.ArrayList")
				  .build();
		ObjectMapper mapper = new ObjectMapper();
		//mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
		var vstag = new VendorSpecificTag();
		vstag.setVendorid("someid".getBytes());
		vstag.setTag_type(new byte[] {(byte)1,(byte)2});
		List<Subtag> l = new ArrayList<>();
		var st1 = new CircuitId();
		st1.setSubtag_value("circuit id 1".getBytes());
		st1.setSubtag_Type((byte)1);
		l.add(st1);
		vstag.setSubtags(l);
		
		String json = mapper.writeValueAsString(vstag);
		System.out.println(json);
		
		
	}
	
	public static class Person {
		private List<Integer> list = new ArrayList<>();
	}

}
