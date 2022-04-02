package com.joey.databasemanager.protocol.jap;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ASN1IntegerSerializer extends JsonSerializer<ASN1Integer> {

	@Override
	public void serialize(ASN1Integer value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeNumber(value.getValue());
	}

}
