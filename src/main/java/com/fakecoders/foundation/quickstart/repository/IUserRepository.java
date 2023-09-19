package com.fakecoders.foundation.quickstart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fakecoders.foundation.quickstart.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
		
	public User findByEmail(String email);

	public User findByVerificationCode(String verificationCode);
}
