package com.fakecoders.foundation.quickstart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fakecoders.foundation.quickstart.model.token.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

}
