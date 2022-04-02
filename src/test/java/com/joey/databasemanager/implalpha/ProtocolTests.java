package com.joey.databasemanager.implalpha;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.databasemanager.protocol.jap.ASN1Integer;
import com.joey.databasemanager.protocol.jap.ASN1Sequence;
import com.joey.databasemanager.protocol.jap.Age;
import com.joey.databasemanager.protocol.jap.Contact;
import com.joey.databasemanager.protocol.jap.ConversionUtils;
import com.joey.databasemanager.protocol.jap.Decoder;
import com.joey.databasemanager.protocol.jap.DecoderException;
import com.joey.databasemanager.protocol.jap.Encoder;
import com.joey.databasemanager.protocol.jap.Name;
import com.joey.databasemanager.protocol.jap.Phone;
import com.joey.io.BitBuffer;

public class ProtocolTests {
	
	public static class DecodeContext {
		private Map<Class<?>,AbstractDecoder<?>> decoders = new HashMap<>();
		
		public DecodeContext() {
			decoders.put(ASN1Integer.class, new ASN1IntegerDecoder());
		}
		
		public AbstractDecoder<?> getDecoder(Class<?> cls) {
			return decoders.get(cls);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T decode(BitBuffer buffer, Class<T> cls) throws DecoderException {
			return (T) decoders.get(cls).decode(buffer, this, cls);
		}
	}
	
	//@Test
	public void test1() throws Exception {
		Contact contact = new Contact(new Name("John Smith"),new Phone("9686125684"));
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(contact));
		
		String j = """
								{
				  "version" : 1,
				  "filler1" : 0,
				  "name" : "John Smith",
				  "phone" : "9686125684"
				}

								""";
		Contact c = mapper.readValue(j, Contact.class);
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(c));
		
	}
	
	
	@Test
	public void test2() throws Exception {
		ASN1Integer age = new Age(74);
		
		Encoder encoder = new Encoder();
		byte[] b = encoder.encode1(age);
		System.out.println(ConversionUtils.toString(b));
	}
	
	
	
	public static class ASN1SequenceDecoder extends AbstractDecoder<ASN1Sequence> {

		@Override
		public ASN1Sequence decode(BitBuffer b, DecodeContext ctxt, Class<?> cl) throws DecoderException {
			return null;
		}

		@Override
		public ASN1Sequence decodeValue(BitBuffer buffer, DecodeContext ctxt, Class<?> cl) throws DecoderException {
			return null;
		}
		
	}
	
	

}
