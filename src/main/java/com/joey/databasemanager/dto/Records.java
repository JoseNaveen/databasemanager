package com.joey.databasemanager.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Records {
	
 
 @JsonProperty
 private List<String> expiryDates;
 public List<String> getExpiryDates() {
	return expiryDates;
}

public void setExpiryDates(List<String> expiryDates) {
	this.expiryDates = expiryDates;
}

@JsonProperty
 private List<DataContent> data = new ArrayList < DataContent > ();
 public List<DataContent> getData() {
	return data;
}

public void setData(List<DataContent> data) {
	this.data = data;
}

@JsonProperty
 private String timestamp;
 @JsonProperty
 private float underlyingValue;
 
 @JsonProperty
 private ArrayList < Object > strikePrices = new ArrayList < Object > ();


 // Getter Methods 

 public String getTimestamp() {
  return timestamp;
 }

 public float getUnderlyingValue() {
  return underlyingValue;
 }

 // Setter Methods 

 public void setTimestamp(String timestamp) {
  this.timestamp = timestamp;
 }

 public void setUnderlyingValue(float underlyingValue) {
  this.underlyingValue = underlyingValue;
 }
}