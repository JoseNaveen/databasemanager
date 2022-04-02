package com.joey.databasemanager.classicmodelsbeans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="employees")
public class Employee {
	
	@Id
	private Integer employeeNumber;
	
	private String firstName;
	
	private String lastName;
	
	private String extension;
	
	private String email;
	
	private String jobTitle;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		sb.append("employeeNumber: " + this.employeeNumber);
		sb.append(" ,firstName: " + this.firstName);
		sb.append(" ,lastName: "+ this.lastName);
		sb.append(" ,jobTitle: " + this.jobTitle);
		sb.append(" ,reportsTo: " + this.reportsTo);
		sb.append(" ,office: " + this.office );
		sb.append(" }");
		
		return sb.toString();
	}
	
	@ManyToOne
	@JoinColumn(name="reports_to")
	private Employee reportsTo;
	
	@ManyToOne
	@JoinColumn(name="office_code")
	private Office office;

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Employee getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Employee reportsTo) {
		this.reportsTo = reportsTo;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	

}
