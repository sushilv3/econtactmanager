package com.econtact.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.econtact.entities.Contact;
import com.econtact.entities.User;
import com.econtact.helper.Message;
import com.econtact.repositories.ContactRepository;
import com.econtact.repositories.UserRepository;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	// execute before every handler call "method for adding common data to response"
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String userName = principal.getName();
		System.out.println(" INSIDE USER CONTROLL => DASHBOARD METHOD USER NAME : " + userName);
		// get the userName(email)

		User user = userRepository.GetUserByUserName(userName);
		System.out.println("INSIDE USER CONTROLL => DASHBOARD METHOD USER OBJECT : " + user);

		model.addAttribute("user", user);
	}

	// dashboard home
	@RequestMapping("/index")
	public String dashBoard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");

		return "normal/user_dashboard";

	}

	// open add form handler
	@GetMapping("add-contact")
	public String addContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// proessing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			String userName = principal.getName();
			User user = this.userRepository.GetUserByUserName(userName);

			contact.setUser(user);

			// processing and uploading file...

			if (file.isEmpty()) {
				// if the file is empty then try our message
				System.out.println("file is empty");

			} else {
				// upload file to folder and update the name to contact
				contact.setImgUrl(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is Uploaded" + contact.getImgUrl());

			}

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("CONTACT OBJECT : " + contact);

			System.out.println("Added to data Base");
			System.out.println("User ::::: " + user);
			
			//message success.....
			
			session.setAttribute("message", new Message("Your Contact is added !! Add more..", "success"));
			
			
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
			//message error
			session.setAttribute("message", new Message("Something went wrong .... try again", "danger"));
			
		}

		return "normal/add_contact_form";
	}
	
	//show contacts handler
	@GetMapping("/show-contacts")
	public String showContacts(Model model,Principal principal) {
		model.addAttribute("title","Show User Contacts");
		//sent contacts list
		String username = principal.getName();
		User user = this.userRepository.GetUserByUserName(username);
		
		List<Contact> contacts = this.contactRepository.findContactByUser(user.getId());
		model.addAttribute("contacts",contacts);
		System.out.println("@@@@@ called inside show-contact handler ===== Conact List ::::  "+contacts);
		return "normal/show_contacts";
	}

}
