package com.fakecoders.foundation.quickstart.custom.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fakecoders.foundation.quickstart.custom.model.CustomUserDetails;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.repository.IUserRepository;

public class CustomUserDetailsServiceImpl implements UserDetailsService {
	 private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

	@Autowired
	private IUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);

		if(user==null) {
			throw new UsernameNotFoundException("user not found");
		}else {
			return new CustomUserDetails(user);
		}
	}
	
	 public UserDetails loadUserById(Integer id) {
	        Optional<User> dbUser = userRepository.findById(id);
	        logger.info("Fetched user : " + dbUser + " by " + id);
	        return dbUser.map(CustomUserDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
	    }

}
