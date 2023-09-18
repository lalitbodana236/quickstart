package com.fakecoders.foundation.vamana.service;

import com.fakecoders.foundation.vamana.model.User;

public interface IUserService {
	
	public User save(User user);
	
	public void removeSessionMessage();
	
	public void sendEmail(User user,String path);
	
	public boolean verifyAccount(String verificationCode);
	
}
