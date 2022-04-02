package com.joey.databasemanager.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		System.out.println("logging out user");
		session.invalidate();
		return "login";
	}
}
