package com.joey.databasemanager.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.joey.databasemanager.classicmodelsbeans.ProductLine;
import com.joey.databasemanager.repository.sql.ProductLineRepository;

@RestController
public class ProductLineApiController {
	
	@Autowired
	private ProductLineRepository productLineRepo;
	
	@GetMapping("/api/productline/{name}")
	public ProductLine getProductLineByName(@PathVariable String name) {
		return productLineRepo.searchByplinename(name);
	}
	
}
