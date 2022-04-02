package com.joey.databasemanager.protocol.jap;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ASN1NumericStringDeserializer<T extends ASN1NumericString> extends JsonDeserializer<ASN1NumericString> {

	@Override
	public ASN1NumericString deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		System.out.println(ctxt.getContextualType());
		return null;
	}

}
