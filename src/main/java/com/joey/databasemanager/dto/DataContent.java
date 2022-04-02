package com.joey.databasemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataContent {
	
@JsonProperty
 private float strikePrice;
@JsonProperty
 private String expiryDate;
@JsonProperty
 CE CE;
@JsonProperty
 PE PE;


 // Getter Methods 

 public float getStrikePrice() {
  return strikePrice;
 }

 public String getExpiryDate() {
  return expiryDate;
 }

 public CE getCE() {
  return CE;
 }

 public PE getPE() {
  return PE;
 }

 // Setter Methods 

 public void setStrikePrice(float strikePrice) {
  this.strikePrice = strikePrice;
 }

 public void setExpiryDate(String expiryDate) {
  this.expiryDate = expiryDate;
 }

 public void setCE(CE CEObject) {
  this.CE = CEObject;
 }

 public void setPE(PE PEObject) {
  this.PE = PEObject;
 }
}