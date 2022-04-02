package com.joey.databasemanager.protocol.jap;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Length(size = 1, unit = Unit.BYTE)
@Tag(value = 11,
	size=1,
	unit = Unit.BYTE)
@JsonPropertyOrder({"version","filler0","name","phone"})
public class Contact extends ASN1Sequence {
	
	@JsonSerialize(using = ASN1IntegerSerializer.class)
	private static class Version extends ASN1Integer {
		
		final private int value;
		
		public Version(int v) {
			this.value = v;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	@JsonSerialize(using = ASN1IntegerSerializer.class)
	private static class Filler0 extends ASN1Integer {
		
		final private int value;
		
		public Filler0(int v) {
			this.value = v;
		}
		public int getValue() {
			return this.value;
		}
	}
	
	@Encoding(size = 4)
	@JsonProperty
	private Version version = new Version(1);
	
	@Encoding(size = 4)
	@JsonProperty
	private Filler0 filler0 = new Filler0(0);
	
	@JsonProperty
	@JsonSerialize(using = ASN1VisibleStringSerializer.class)
	private Name name;
	
	@JsonProperty
	@JsonSerialize(using = ASN1NumericStringSerializer.class)
	private Phone phone;
	
	public Contact(Name n,Phone p) {
		this.name = n;
		this.phone = p;
	}
	
	public Contact() {

	}
	
	
	@JsonIgnore
	public List<ASN1Element> getValue() {
		
		ArrayList<ASN1Element> list = new ArrayList<ASN1Element>();
		
		if (name!=null) {
			list.add(name);
		}
		if (phone!=null) {
			list.add(phone);
		}
		return list;
	}	
	
	
}


