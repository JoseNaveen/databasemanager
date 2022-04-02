package com.joey.databasemanager.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.joey.databasemanager.classicmodelsbeans.Office;
import com.joey.databasemanager.exceptions.ApplicationException;
import com.joey.databasemanager.exceptions.OfficeNotFoundException;
import com.joey.databasemanager.repository.sql.OfficeRepository;

@RestController
public class OfficeApiController {
	
	@Autowired
	private OfficeRepository officeRepo;
	
	@GetMapping("/api/office/{officecode}")
	public Office getOfficeById(@PathVariable String officecode) {
		return this.officeRepo.findById(officecode).orElseThrow(()-> new OfficeNotFoundException("Invalid office"));
		
	}
}
