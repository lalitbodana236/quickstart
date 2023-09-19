package com.fakecoders.foundation.quickstart.service;

import com.fakecoders.foundation.quickstart.model.User;

public interface IUserService {
	
	public User save(User user);
	
	public void removeSessionMessage();
	
	public void sendEmail(User user,String path);
	
	public boolean verifyAccount(String verificationCode);
	
}
