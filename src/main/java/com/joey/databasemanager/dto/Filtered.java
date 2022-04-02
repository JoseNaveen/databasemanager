package com.joey.databasemanager.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Filtered {

	@JsonProperty
	ArrayList<Object> data = new ArrayList<Object>();
	@JsonProperty
	CEOIVOL CE;
	@JsonProperty
	PEOIVOL PE;

	// Getter Methods

	public CEOIVOL getCE() {
		return CE;
	}

	public PEOIVOL getPE() {
		return PE;
	}

	// Setter Methods

	public void setCE(CEOIVOL CEObject) {
		this.CE = CEObject;
	}

	public void setPE(PEOIVOL PEObject) {
		this.PE = PEObject;
	}
}