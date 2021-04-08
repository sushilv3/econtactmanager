package com.econtact.controllers;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.econtact.entities.User;
import com.econtact.repositories.UserRepository;
import com.econtact.services.EmailService;

@Controller
public class forgotController {

	Random random =new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
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
			session.setAttribute("myotp", otp);
			session.setAttribute("emial", email);
			return "verify_otp";
		}
		else {
			
			session.setAttribute("message", "Check Your Email Id");
			return "forgot_email_form";	
		}
		
	}
	
	//verify otp
	
	@PostMapping("/verify-otp")
	public String verifyotp (@RequestParam("otp") int otp, HttpSession session) {
		
		int myOtp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		
		if(myOtp==otp) {
			
			//password change form
			User user = this.userRepository.getUserByUserName(email);
			
			if(user==null) {
				//sent error message
				
				session.setAttribute("message", "User does not exist with this email");
				return "forgot_email_form";
				
			}else {
//				sent change password form
			}
			
			return "password_change_form";
			
		}
		else {
			session.setAttribute("message", "You Have Entered Wrong OTP");
			return "verify_otp";
		}
		
	}
	
	
}
