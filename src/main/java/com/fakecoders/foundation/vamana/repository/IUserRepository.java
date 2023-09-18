package com.fakecoders.foundation.vamana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fakecoders.foundation.vamana.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
		
	public User findByEmail(String email);

	public User findByVerificationCode(String verificationCode);
}
