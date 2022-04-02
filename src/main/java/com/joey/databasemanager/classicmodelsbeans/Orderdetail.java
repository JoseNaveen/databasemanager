package com.joey.databasemanager.classicmodelsbeans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="orderdetails")
public class Orderdetail implements Serializable{
	
	
	@EmbeddedId
	private OrderDetailId orderdetailid;
	
	private int quantityOrdered;
	
	@Column(precision=10, scale=2)
	private double priceEach;
	
	
	private short orderLineNumber;

	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{ ");
		sb.append(this.orderdetailid.toString());
		sb.append(" ,priceEach: "+ this.priceEach +" ,orderLineNumber: " + this.orderLineNumber +", quantOrdered: " + this.quantityOrdered);
		sb.append("}");
		return sb.toString();
	}

	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}


	public double getPriceEach() {
		return priceEach;
	}


	public void setPriceEach(double priceEach) {
		this.priceEach = priceEach;
	}


	public short getOrderLineNumber() {
		return orderLineNumber;
	}


	public void setOrderLineNumber(short orderLineNumber) {
		this.orderLineNumber = orderLineNumber;
	}
	

}
