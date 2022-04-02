package com.joey.databasemanager.nse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTests {
	
	
	@Test
	public void test1() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		String input = """
				{
					"records": {
						"expiryDates": ["123","245"]
						}
				}
				""";
		Content c = mapper.readValue(input,Content.class);
		System.out.println(c);
	}
	
	static class Content {
		@JsonProperty
		private Records records;
		public String toString() {
			return "records: [" + records.toString() + "]";
		}
	}
	
	static class Records {
		@JsonProperty
		List<String> expiryDates;
		public String toString() {
			return "expiryDates: " + expiryDates.get(1);
		}
	}
	
	
	

}
