package com.econtact.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.econtact.entities.User;
import com.econtact.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/index")
	public String dashBoard(Model model,Principal principal) {
		
		String userName = principal.getName();
		System.out.println(" INSIDE USER CONTROLL => DASHBOARD METHOD USER NAME : " + userName);
		//get the userName(email)
		
		User user = userRepository.GetUserByUserName(userName);
		System.out.println("INSIDE USER CONTROLL => DASHBOARD METHOD USER OBJECT : "+ user);
		
		model.addAttribute("user", user);
		return "normal/user_dashboard";

	}
}
