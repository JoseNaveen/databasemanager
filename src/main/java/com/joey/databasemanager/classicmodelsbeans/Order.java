package com.joey.databasemanager.classicmodelsbeans;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="orders")
public class Order {
	
	
	@Id
	private Integer orderNumber;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date orderDate;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date requiredDate;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date shippedDate;
	
	private String status;
	
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="customer_number")
	private Customer customerNumber;

	private int hashcode = 0;
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		sb.append("orderNumber: " + this.orderNumber);
		sb.append(" ,orderDate: " + this.orderDate);
		sb.append(" ,Customer: " + this.customerNumber);
		sb.append(" }");
		
		return sb.toString();
	}
	
	public int hashCode() {
		if (hashcode!=0) {
			return hashcode;
		}
		
		hashcode = Integer.hashCode(this.orderNumber);
		hashcode = 31*hashcode + orderDate.hashCode();
		hashcode = 31*hashcode + shippedDate.hashCode();
		hashcode = 31*hashcode + requiredDate.hashCode();
		
		return hashcode;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public java.util.Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}

	public java.util.Date getRequiredDate() {
		return requiredDate;
	}

	public void setRequiredDate(java.util.Date requiredDate) {
		this.requiredDate = requiredDate;
	}

	public java.util.Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(java.util.Date shippedDate) {
		this.shippedDate = shippedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Customer getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Customer customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	

}
