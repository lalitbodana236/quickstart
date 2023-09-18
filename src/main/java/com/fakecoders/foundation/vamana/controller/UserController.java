package com.fakecoders.foundation.vamana.controller;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fakecoders.foundation.vamana.model.User;
import com.fakecoders.foundation.vamana.repository.IUserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserRepository userRepo;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/profile")
	public String profile() {
		return "profile";
	}

}