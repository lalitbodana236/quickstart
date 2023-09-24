package com.fakecoders.foundation.quickstart.service;

import java.util.List;
import java.util.Optional;

import com.fakecoders.foundation.quickstart.custom.model.CustomUserDetails;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.model.UserDevice;
import com.fakecoders.foundation.quickstart.model.payload.LogOutRequest;
import com.fakecoders.foundation.quickstart.model.payload.RegistrationRequest;
import com.fakecoders.foundation.quickstart.model.payload.UserInfoResponse;
import com.fakecoders.foundation.quickstart.model.token.RefreshToken;

import jakarta.validation.Valid;

public interface IUserService {
	
	public User save(User user);
	
	public void removeSessionMessage();
	
	public void sendEmail(User user,String path);
	
	public boolean verifyAccount(String verificationCode);

	public Boolean existsByEmail(String email);

	public Boolean existsByUsername(String username);
	
	Optional<User> findByEmail(String username);

	public User createUser(RegistrationRequest newRegistrationRequest);
	
	Optional<User> findByUserId(Integer id);

	public void logoutUser(CustomUserDetails customUserDetails, @Valid LogOutRequest logOutRequest);

	public List<User> findAllUser();

	UserInfoResponse getUserInfo(String email);
	
}
