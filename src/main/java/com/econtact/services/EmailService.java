package com.econtact.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String to, String body, String subject) {

		boolean f = false;

		SimpleMailMessage message = new SimpleMailMessage();
		try {
			message.setFrom("sushilv3rma@gmail.com");
			message.setTo(to);
			message.setText(body);
			message.setSubject(subject);

			mailSender.send(message);

			System.out.println("Mail Sent....");
			
			f=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

}
