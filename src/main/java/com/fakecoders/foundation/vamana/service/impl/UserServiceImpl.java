package com.fakecoders.foundation.vamana.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.fakecoders.foundation.vamana.config.SmtpMailSender;
import com.fakecoders.foundation.vamana.model.User;
import com.fakecoders.foundation.vamana.repository.IUserRepository;
import com.fakecoders.foundation.vamana.service.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.mail.internet.MimeMessage;


@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*
	 * @Autowired private JavaMailSender mailSender;
	 */

	@Autowired
	SmtpMailSender mail;

	@Override
	public User save(User user) {
		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole("ROLE_USER");

		return userRepository.save(user);
	}

	@Override
	public void removeSessionMessage() {
		HttpSession session = 	((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		session.removeAttribute("msg");
	}

	@Override
	public void sendEmail(User user, String url) {
		String to = user.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Coders Army";

		try {
			content = content.replace("[[name]]", user.getFirstname()+" "+user.getLastname());
			String siteUrl = url + "/verify?code=" + user.getVerificationCode();
			System.out.println(siteUrl);
			content = content.replace("[[URL]]", siteUrl);
			mail.sendMail(to, subject, content);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean verifyAccount(String verificationCode) {

		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null) {
			return false;
		} else {

			user.setEnable(true);
			user.setVerificationCode(null);

			userRepository.save(user);

			return true;
		}

	}

}
