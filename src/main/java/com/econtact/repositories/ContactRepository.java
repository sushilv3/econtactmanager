package com.econtact.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.econtact.entities.Contact;
import com.econtact.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	//pagination....
	@Query("from Contact as c where c.user.id = :userId")
	
	//currentPage = page
	//Contact per apge = 2
	public Page<Contact> findContactByUser(@Param("userId")int userId, Pageable pageable);
	
	//predefine search method
	public List<Contact> findByNameContainingAndUser(String name, User user);



}
