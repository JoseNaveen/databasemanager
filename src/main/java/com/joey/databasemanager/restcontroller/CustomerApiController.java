package com.joey.databasemanager.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joey.databasemanager.classicmodelsbeans.CustomerPayments;
import com.joey.databasemanager.repository.sql.CustomerRepository;

@RestController
public class CustomerApiController {

	@Autowired
	private CustomerRepository customerRepo;
	
	@GetMapping("/api/customersandtotalpayments")
	public List<CustomerPayments> getCustPayments() {
		return customerRepo.getCustomersTotalNumOfPayments();
	}
}
