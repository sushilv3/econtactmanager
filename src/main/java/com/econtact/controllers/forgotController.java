package com.econtact.controllers;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.econtact.services.EmailService;

@Controller
public class forgotController {

	Random random =new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	//emmail id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		
		
		return "forgot_email_form";
	}
	
	//send-otp 
	@PostMapping("/send-otp")
	public String sentOtp(@RequestParam("email") String email, HttpSession session) {
		
		System.out.println("*****Email ***** " + email);
		
		//generating otp of 4 digit
		
		int otp = random.nextInt(9999);
		System.out.println("^^^^^^ OTP : "+otp);
		
		//write code for send otp to email...
		
		String to = email;
		
		String subject = "OTP from econtact manager"; 
		
		String body = " Otp = "+otp ;
		
		boolean flag = this.emailService.sendEmail(to, body, subject);
		System.out.println("%%%%% FLAG VALUE  " +flag);
		
		if(flag)
		{
			session.setAttribute("otp", otp);
			return "verify_otp";
		}
		else {
			
			session.setAttribute("message", "Check Your Email Id");
			return "forgot_email_form";	
		}
		
	}
	
}
