package com.joey.databasemanager.dto;

public class NSEContent {
 Records records;
 Filtered filtered;


 // Getter Methods 

 public Records getRecords() {
  return records;
 }

 public Filtered getFiltered() {
  return filtered;
 }

 // Setter Methods 

 public void setRecords(Records recordsObject) {
  this.records = recordsObject;
 }

 public void setFiltered(Filtered filteredObject) {
  this.filtered = filteredObject;
 }
}