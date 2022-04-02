package com.joey.databasemanager.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joey.databasemanager.classicmodelsbeans.Customer;
import com.joey.databasemanager.classicmodelsbeans.CustomerPayments;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	
	@Query(value="""
			select c.contact_first_name, c.contact_last_name, new_table.tot_num_payments from customers c
			inner join
			(select count(*) tot_num_payments, customer_number from payments
			group by customer_number) new_table
			on new_table.customer_number=c.customer_number;
			""",nativeQuery=true)
	public List<CustomerPayments> getCustomersTotalNumOfPayments();
}
