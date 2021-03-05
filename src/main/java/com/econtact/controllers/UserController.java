package com.econtact.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.econtact.entities.Contact;
import com.econtact.entities.User;
import com.econtact.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	//execute before every handler call "method for adding common data to response"
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String userName = principal.getName();
		System.out.println(" INSIDE USER CONTROLL => DASHBOARD METHOD USER NAME : " + userName);
		//get the userName(email)
		
		User user = userRepository.GetUserByUserName(userName);
		System.out.println("INSIDE USER CONTROLL => DASHBOARD METHOD USER OBJECT : "+ user);
		
		model.addAttribute("user", user);
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashBoard(Model model,Principal principal) {
		model.addAttribute("title","User Dashboard");
		
		return "normal/user_dashboard";

	}
	//open add form handler
	@GetMapping("add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
}
