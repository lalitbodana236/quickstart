package com.fakecoders.foundation.vamana.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fakecoders.foundation.vamana.custom.model.CustomUserDetails;
import com.fakecoders.foundation.vamana.model.User;
import com.fakecoders.foundation.vamana.repository.IUserRepository;

public class CustomUserDetailsServiceImpl implements UserDetailsService {

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

}