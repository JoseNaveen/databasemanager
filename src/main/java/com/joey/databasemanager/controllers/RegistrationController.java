package com.joey.databasemanager.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.joey.databasemanager.beans.User;
import com.joey.databasemanager.repository.sql.UserRepository;

@Controller
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "dateOfBirth", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
		
	}
	
	@PostMapping("/registeruser")
	public String registeruser(@Valid @ModelAttribute("newuser") User user, BindingResult result,Model model) {
		if (result.hasErrors()) {
			return "register";
		}
		userRepository.save(user);
		
		model.addAttribute("registrationStatus", "Sucessfully registered user");
		return "login";
	}
	
	
}
