package com.fakecoders.foundation.quickstart.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.fakecoders.foundation.quickstart.annotation.CurrentUser;
import com.fakecoders.foundation.quickstart.config.SmtpMailSender;
import com.fakecoders.foundation.quickstart.custom.model.CustomUserDetails;
import com.fakecoders.foundation.quickstart.exception.UserLogoutException;
import com.fakecoders.foundation.quickstart.model.Role;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.model.UserDevice;
import com.fakecoders.foundation.quickstart.model.payload.LogOutRequest;
import com.fakecoders.foundation.quickstart.model.payload.RegistrationRequest;
import com.fakecoders.foundation.quickstart.repository.IUserRepository;
import com.fakecoders.foundation.quickstart.service.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.mail.internet.MimeMessage;


@Service
public class UserServiceImpl implements IUserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 private final UserDeviceService userDeviceService;
	 
	 private final RefreshTokenService refreshTokenService;

	 
	 
	public UserServiceImpl(UserDeviceService userDeviceService, RefreshTokenService refreshTokenService) {
		super();
		this.userDeviceService = userDeviceService;
		this.refreshTokenService = refreshTokenService;
	}

	/*
	 * @Autowired private JavaMailSender mailSender;
	 */


	@Override
	public User save(User user) {
		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		user.setRole(Role.USER);

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
		//	mail.sendMail(to, subject, content);

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
	
	 /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for this user, throw a log out exception.
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutRequest logOutRequest) {
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        UserDevice userDevice = userDeviceService.findDeviceByUserId(currentUser.getUserid(), deviceId)
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));

        logger.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }
    
    /**
     * Check is the user exists given the email: naturalId
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check is the user exists given the username: naturalId
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByEmail(username);
    }

	@Override
	public Optional<User> findByEmail(String username) {
		return Optional.of(userRepository.findByEmail(username));
	}
	
	 /**
     * Creates a new user from the registration request
     */
    public User createUser(RegistrationRequest registerRequest) {
        User newUser = new User();
      //  Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(registerRequest.getPassword());
        //newUser.setUsername(registerRequest.getUsername());
       // newUser.addRoles(getRolesForNewUser(isNewUserAsAdmin));
        newUser.setEnable(true);
        newUser.setIsEmailVerified(false);
        return newUser;
    }

	@Override
	public Optional<User> findByUserId(Integer id) {
		return userRepository.findById(id);
	}


}
