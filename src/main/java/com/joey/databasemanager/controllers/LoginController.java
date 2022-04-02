package com.joey.databasemanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.joey.databasemanager.beans.Login;
import com.joey.databasemanager.beans.User;
import com.joey.databasemanager.exceptions.ApplicationException;
import com.joey.databasemanager.repository.sql.UserRepository;

@Controller
@SessionAttributes("login")
public class LoginController {

	@Autowired
	private UserRepository userRepository;
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute("login") Login login) {
		
		User user = userRepository.searchByName(login.getUsername());
		if (user == null) {
			throw new ApplicationException("User does not Exist");
		}
		return "forward:/userprofile";
	}
	
	/*
	 * @ExceptionHandler(ApplicationException.class) public String handler() {
	 * return "error"; }
	 */
}
