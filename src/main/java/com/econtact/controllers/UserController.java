package com.econtact.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/index")
	public String dashBoard() {
		

		return "normal/user_dashboard";

	}
}
