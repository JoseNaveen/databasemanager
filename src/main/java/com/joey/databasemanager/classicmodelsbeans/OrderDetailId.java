package com.joey.databasemanager.classicmodelsbeans;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Embeddable
public class OrderDetailId implements Serializable{


	private static final long serialVersionUID = 8920876027994015724L;

	@OneToOne
	@JoinColumn(name="order_number")
	private Order orderNumber;
	
	@ManyToOne
	@JoinColumn(name="product_code")
	private Product productCode;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ ");
		sb.append(" Order: " + this.orderNumber);
		sb.append(" Product: " + this.productCode);
		sb.append("}");
		return sb.toString();
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof OrderDetailId)) {
			return false;
		}
		OrderDetailId target = (OrderDetailId) obj;
		if (this.orderNumber.getOrderNumber()!=target.orderNumber.getOrderNumber() 
				||
				this.productCode.getProductCode().equals(target.productCode.getProductCode())) {
			return false;
		}
		return true;
	}
	
	public int hashCode() {
		return Objects.hash(productCode,orderNumber);
	}
}
