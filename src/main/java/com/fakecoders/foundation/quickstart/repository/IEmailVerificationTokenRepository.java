package com.fakecoders.foundation.quickstart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fakecoders.foundation.quickstart.model.token.EmailVerificationToken;

import java.util.Optional;

public interface IEmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);
}
