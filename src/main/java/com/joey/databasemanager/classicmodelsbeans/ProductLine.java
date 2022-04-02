package com.joey.databasemanager.classicmodelsbeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="productlines")
public class ProductLine {
	
	@Id
	@Column(name = "productLine", columnDefinition="VARCHAR(50)")
	private String productLine;
	
	@Column(name = "textDescription", columnDefinition="VARCHAR(4000)")
	private String textDescription;
	
	@Column(columnDefinition = "MEDIUMTEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String htmlDescription;
	
	
	@Lob
    @Column(name = "image", columnDefinition="BLOB")
	private byte[] image;
	
	public String getProductLine() {
		return productLine;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		sb.append("productLine: " + this.productLine + ", ");
		sb.append("textDescription: " + this.textDescription + ", ");
		sb.append("htmlDescription: " + this.htmlDescription);
		sb.append(" }");
		
		return sb.toString();
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	

}
