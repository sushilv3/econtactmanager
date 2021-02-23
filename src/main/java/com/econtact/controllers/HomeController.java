package com.econtact.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.econtact.repositories.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
	public String test() {
		return "working";
	}
	
}
