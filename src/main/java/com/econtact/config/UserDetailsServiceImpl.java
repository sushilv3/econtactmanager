package com.econtact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.econtact.entities.User;
import com.econtact.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		//fetching user from database
		
		User user = userRepository.GetUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetailsImp customUserDetailsImp = new CustomUserDetailsImp(user);
		return customUserDetailsImp;
	}

}
