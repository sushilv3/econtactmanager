package com.econtact.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.econtact.entities.Contact;
import com.econtact.entities.User;
import com.econtact.repositories.ContactRepository;
import com.econtact.repositories.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	//search handler
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable ("query") String query, Principal principal){
		
		System.out.println(query);
		
		User user = userRepository.GetUserByUserName(principal.getName());
		
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
		
	}

}
