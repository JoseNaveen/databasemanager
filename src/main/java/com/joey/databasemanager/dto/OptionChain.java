package com.joey.databasemanager.dto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="option_chain")
public class OptionChain {
	
	private ObjectId id;
	
	private String stock_name;
	
	private NSEContent nsecontent;
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getStock_name() {
		return stock_name;
	}

	public void setStock_name(String stock_name) {
		this.stock_name = stock_name;
	}

	public NSEContent getNsecontent() {
		return nsecontent;
	}

	public void setNsecontent(NSEContent nsecontent) {
		this.nsecontent = nsecontent;
	}
	
	

}
