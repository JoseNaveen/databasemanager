package com.joey.databasemanager.classicmodelsbeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="products")
public class Product {
	
	
	@Id
	private String productCode;
	
	private String productName;
	
	@ManyToOne
	@JoinColumn(name="product_line")
	private ProductLine productLine;
	
	private String productScale;
	
	private String productVendor;
	
	private String productDescription;
	
	private short quantityInStock;
	
	@Column(precision=10, scale=2)
	private double buyPrice;
	
	@Column(precision=10, scale=2)
	private double msrp;
		
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ ");
		sb.append(" productCode: "+ this.productCode);
		sb.append(" productName: " +this.productName);
		sb.append("}");
		return sb.toString();
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductLine getProductLine() {
		return productLine;
	}

	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
	}

	public String getProductScale() {
		return productScale;
	}

	public void setProductScale(String productScale) {
		this.productScale = productScale;
	}

	public String getProductVendor() {
		return productVendor;
	}

	public void setProductVendor(String productVendor) {
		this.productVendor = productVendor;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public short getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(short quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getMsrp() {
		return msrp;
	}

	public void setMsrp(double msrp) {
		this.msrp = msrp;
	}
	
	
	

}
