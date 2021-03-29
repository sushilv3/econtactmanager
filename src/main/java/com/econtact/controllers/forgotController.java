package com.econtact.controllers;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class forgotController {

	Random random =new Random(1000);
	
	//emmail id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		
		
		return "forgot_email_form";
	}
	
	//send-otp 
	@PostMapping("/send-otp")
	public String sentOtp(@RequestParam("email") String email) {
		
		System.out.println("*****Email ***** " + email);
		
		//generating otp of 4 digit
		
		int otp = random.nextInt(9999);
		System.out.println("^^^^^^ OTP : "+otp);
		
		//write code for send otp to email...
		return "verify_otp";
	}
	
}
