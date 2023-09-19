package com.fakecoders.foundation.quickstart.controller;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.UUID;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fakecoders.foundation.quickstart.config.SmtpMailSender;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.repository.IUserRepository;
import com.fakecoders.foundation.quickstart.service.IUserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private IUserService userService;



	@Autowired
	private IUserRepository userRepo;

	@Autowired
	SmtpMailSender mail;

	@Autowired
	private StringEncryptor encryptor;


	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/")
	public String index() throws MessagingException {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	/*
	@GetMapping("/user/profile")
	public String profile(Principal p, Model m) {
		String email = p.getName();
		User user = userRepo.findByEmail(email);
		m.addAttribute("user", user);
		return "profile";
	}
	 */
	@GetMapping("/user/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/home")
	public String homeOne() {
		return "home";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session, Model m,HttpServletRequest request) {

		// System.out.println(user);
		user.setEnable(false);
		user.setVerificationCode(UUID.randomUUID().toString());
		String url = request.getRequestURL().toString();

		// System.out.println(url); http://localhost:8080/saveUser
		url = url.replace(request.getServletPath(), "");

		User u = userService.save(user);
		userService.sendEmail(user, url);


		if (u != null) {
			// System.out.println("save sucess");
			session.setAttribute("msg", "Register successfully");

		} else {
			// System.out.println("error in server");
			session.setAttribute("msg", "Something wrong server");
		}
		return "redirect:/register";
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model m) {
		boolean f = userService.verifyAccount(code);

		if (f) {
			m.addAttribute("msg", "Sucessfully your account is verified");
		} else {
			m.addAttribute("msg", "may be your vefication code is incorrect or already veified ");
		}

		return "message";
	}
}
