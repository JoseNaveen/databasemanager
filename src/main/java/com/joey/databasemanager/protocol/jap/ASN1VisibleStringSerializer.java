package com.joey.databasemanager.protocol.jap;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ASN1VisibleStringSerializer extends JsonSerializer<ASN1VisibleString> {

	@Override
	public void serialize(ASN1VisibleString value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeString(value.getValue());
	}

}
