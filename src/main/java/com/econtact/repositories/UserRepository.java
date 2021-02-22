package com.econtact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.econtact.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
