package com.joey.databasemanager.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import com.joey.databasemanager.repository.sql.ProductRepository;

@Controller
public class SearchController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private AsyncTaskExecutor executor;
	
	@GetMapping("/search")
	public DeferredResult<String> search(@RequestParam("search") String search, Model model,HttpServletRequest request) {
		
		DeferredResult<String> deferredResult = new DeferredResult<String>();
		System.out.println("Async supported: "+ request.isAsyncSupported());
		System.out.println("THread container: " + Thread.currentThread().getName());
		
		/*
		 * return () -> { Thread.sleep(3000); System.out.println("THread name: " +
		 * Thread.currentThread().getName()); List<Product> products = new
		 * ArrayList<>(); System.out.println("in search controller");
		 * System.out.println("searching: " + search); products =
		 * productRepository.searchByName(search);
		 * 
		 * 
		 * model.addAttribute("products", products); return "search"; };
		 */
		 
		executor.execute(() -> {
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		System.out.println("THread name: " +Thread.currentThread().getName()); 
		List<com.joey.databasemanager.classicmodelsbeans.Product> products = new
		 ArrayList<>(); System.out.println("in search controller");
				  System.out.println("searching: " + search); 
				  //products = productRepository.searchByName(search);
		 //model.addAttribute("products", products); 
		 //deferredResult.setResult("null"); 
		});
		
		
		
		return deferredResult;
		
	}
	
}
