package com.econtact.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.econtact.entities.Contact;
import com.econtact.entities.User;
import com.econtact.helper.Message;
import com.econtact.repositories.ContactRepository;
import com.econtact.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

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
				contact.setImgUrl("contact.png");

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

			// message success.....

			session.setAttribute("message", new Message("Your Contact is added !! Add more..", "success"));

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
			// message error
			session.setAttribute("message", new Message("Something went wrong .... try again", "danger"));

		}

		return "normal/add_contact_form";
	}

	// show contacts handler
	// per page =2[n]
	// current page =0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show User Contacts");
		// sent contacts list
		String username = principal.getName();
		User user = this.userRepository.GetUserByUserName(username);

		Pageable pageable = PageRequest.of(page, 2);

		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);

		model.addAttribute("currentPage", page);

		model.addAttribute("totalPages", contacts.getTotalPages());

		System.out.println("@@@@@ called inside show-contact handler ===== Conact List ::::  " + contacts);
		return "normal/show_contacts";
	}

	// showing specific contact details handler

	@RequestMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("%%%% called inside showContactDeatil and value of CID : " + cId);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		//
		String userName = principal.getName();
		User user = this.userRepository.GetUserByUserName(userName);
		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		System.out.println("*****Contact :: " + contact);

		return "normal/contact_detail";
	}

	// delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		// unlink contect to user
		// check....

		//
		User user = this.userRepository.GetUserByUserName(principal.getName());
		
		user.getContacts().remove(contact);
		
		this.userRepository.save(user);

		session.setAttribute("message", new Message("Contact deteted successfully...", "success"));

		return "redirect:/user/show-contacts/0";
	}

	// open update form handler

	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId, Model model) {

		model.addAttribute("title", "Update Contact");
		Contact contact = this.contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);
		return "normal/update_form";
	}

	// update contact handler

	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {

		try {
			// fetch old contact detail
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			// image..
			if (!file.isEmpty()) {
				// file work... rewrite

				// delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImgUrl());
				file1.delete();

				// update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImgUrl(file.getOriginalFilename());

			} else {
				contact.setImgUrl(oldContactDetail.getImgUrl());
			}
			User user = this.userRepository.GetUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);

			session.setAttribute("message", new Message("Your Contact is updated...", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(" ##UPDATE CONTACT HANDLER## ");
		System.out.println("Contact Name " + contact.getName());
		System.out.println("Contact ID " + contact.getcId());
		return "redirect:/user/contact/" + contact.getcId();
	}
}
