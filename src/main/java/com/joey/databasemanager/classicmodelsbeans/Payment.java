package com.joey.databasemanager.classicmodelsbeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="payments")
public class Payment {
	
	
	@Id
	private String checkNumber;
	
	@ManyToOne
	@JoinColumn(name="customer_number")
	private Customer customer_number;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date paymentDate;
	
	
	@Column(precision=10, scale=2)
	private double amount;


	public String getCheckNumber() {
		return checkNumber;
	}


	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}


	public Customer getCustomer_number() {
		return customer_number;
	}


	public void setCustomer_number(Customer customer_number) {
		this.customer_number = customer_number;
	}


	public java.util.Date getPaymentDate() {
		return paymentDate;
	}


	public void setPaymentDate(java.util.Date paymentDate) {
		this.paymentDate = paymentDate;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
