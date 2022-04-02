package com.joey.databasemanager.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.joey.databasemanager.beans.Login;
import com.joey.databasemanager.beans.User;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String getHome() {
		return "index";
	}
	
	@GetMapping("/goToSearch")
	public String goToSearch() {
		return "search";
	}
	
	@GetMapping("/goToLogin")
	public String goToLogin() {
		return "login";
	}
	
	@GetMapping("/goToRegistration")
	public String goToRegistration() {
		return "register";
	}
	
	
}
