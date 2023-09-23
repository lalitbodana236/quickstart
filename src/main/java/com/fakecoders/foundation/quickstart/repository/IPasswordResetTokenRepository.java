package com.fakecoders.foundation.quickstart.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fakecoders.foundation.quickstart.model.PasswordResetToken;
import com.fakecoders.foundation.quickstart.model.User;

@Repository
public interface IPasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Query("SELECT t FROM PASSWORD_RESET_TOKEN t WHERE t.active = true and t.user = :user")
    List<PasswordResetToken> findActiveTokensForUser(User user);
}
