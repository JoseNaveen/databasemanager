package com.joey.databasemanager.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.joey.databasemanager.beans.Login;
import com.joey.databasemanager.beans.User;

@ControllerAdvice
public class DefaultModelAttributeController {
	
	
	@ModelAttribute("newuser")
	public User getDefaultUser() {
		var user = new User();
		user.setFirstName("Enter Firstname here");
		user.setLastName("Enter Lastname here");
		user.setUsername("Enter username here");
		user.setPassword("");
		return user;
	}
	
	@ModelAttribute("login")
	public Login getDefaultLogin() {
		var login = new Login();
		login.setUsername("enter a username here");
		login.setPassword("enter a password here"); 	 	
		return login;
	}
	
	
	@ModelAttribute("genderItems") 
	public List<String> getGenderItems() {
		return Arrays.asList(new String[] {"Male","Female","Other"});
	}
}
