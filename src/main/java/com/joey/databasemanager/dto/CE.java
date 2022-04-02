package com.joey.databasemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CE {
	@JsonProperty
	private float strikePrice;
	@JsonProperty
	private String expiryDate;
	@JsonProperty
	private String underlying;
	@JsonProperty
	private String identifier;
	@JsonProperty
	private float openInterest;
	@JsonProperty
	private float changeinOpenInterest;
	@JsonProperty
	private float pchangeinOpenInterest;
	@JsonProperty
	private float totalTradedVolume;
	@JsonProperty
	private float impliedVolatility;
	@JsonProperty
	private float lastPrice;
	@JsonProperty
	private float change;
	@JsonProperty
	private float pChange;
	@JsonProperty
	private float totalBuyQuantity;
	@JsonProperty
	private float totalSellQuantity;
	@JsonProperty
	private float bidQty;
	@JsonProperty
	private float bidprice;
	@JsonProperty
	private float askQty;
	@JsonProperty
	private float askPrice;
	@JsonProperty
	private float underlyingValue;

	// Getter Methods

	public float getStrikePrice() {
		return strikePrice;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getUnderlying() {
		return underlying;
	}

	public String getIdentifier() {
		return identifier;
	}

	public float getOpenInterest() {
		return openInterest;
	}

	public float getChangeinOpenInterest() {
		return changeinOpenInterest;
	}

	public float getPchangeinOpenInterest() {
		return pchangeinOpenInterest;
	}

	public float getTotalTradedVolume() {
		return totalTradedVolume;
	}

	public float getImpliedVolatility() {
		return impliedVolatility;
	}

	public float getLastPrice() {
		return lastPrice;
	}

	public float getChange() {
		return change;
	}

	public float getPChange() {
		return pChange;
	}

	public float getTotalBuyQuantity() {
		return totalBuyQuantity;
	}

	public float getTotalSellQuantity() {
		return totalSellQuantity;
	}

	public float getBidQty() {
		return bidQty;
	}

	public float getBidprice() {
		return bidprice;
	}

	public float getAskQty() {
		return askQty;
	}

	public float getAskPrice() {
		return askPrice;
	}

	public float getUnderlyingValue() {
		return underlyingValue;
	}

	// Setter Methods

	public void setStrikePrice(float strikePrice) {
		this.strikePrice = strikePrice;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setUnderlying(String underlying) {
		this.underlying = underlying;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setOpenInterest(float openInterest) {
		this.openInterest = openInterest;
	}

	public void setChangeinOpenInterest(float changeinOpenInterest) {
		this.changeinOpenInterest = changeinOpenInterest;
	}

	public void setPchangeinOpenInterest(float pchangeinOpenInterest) {
		this.pchangeinOpenInterest = pchangeinOpenInterest;
	}

	public void setTotalTradedVolume(float totalTradedVolume) {
		this.totalTradedVolume = totalTradedVolume;
	}

	public void setImpliedVolatility(float impliedVolatility) {
		this.impliedVolatility = impliedVolatility;
	}

	public void setLastPrice(float lastPrice) {
		this.lastPrice = lastPrice;
	}

	public void setChange(float change) {
		this.change = change;
	}

	public void setPChange(float pChange) {
		this.pChange = pChange;
	}

	public void setTotalBuyQuantity(float totalBuyQuantity) {
		this.totalBuyQuantity = totalBuyQuantity;
	}

	public void setTotalSellQuantity(float totalSellQuantity) {
		this.totalSellQuantity = totalSellQuantity;
	}

	public void setBidQty(float bidQty) {
		this.bidQty = bidQty;
	}

	public void setBidprice(float bidprice) {
		this.bidprice = bidprice;
	}

	public void setAskQty(float askQty) {
		this.askQty = askQty;
	}

	public void setAskPrice(float askPrice) {
		this.askPrice = askPrice;
	}

	public void setUnderlyingValue(float underlyingValue) {
		this.underlyingValue = underlyingValue;
	}
}