package com.joey.databasemanager.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "Java_class")
public class VendorSpecificTag {
	
	
	private byte[] tag_type;
	
	private byte[] vendorid;
	
	private List<Subtag> subtags;

	public byte[] getTag_type() {
		return tag_type;
	}

	public void setTag_type(byte[] tag_type) {
		this.tag_type = tag_type;
	}

	public byte[] getVendorid() {
		return vendorid;
	}

	public void setVendorid(byte[] vendorid) {
		this.vendorid = vendorid;
	}

	public List<Subtag> getSubtags() {
		return subtags;
	}

	public void setSubtags(List<Subtag> subtags) {
		this.subtags = subtags;
	}
	
	

}
