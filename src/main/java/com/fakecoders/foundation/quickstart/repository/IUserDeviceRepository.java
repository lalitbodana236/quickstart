package com.fakecoders.foundation.quickstart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fakecoders.foundation.quickstart.model.UserDevice;
import com.fakecoders.foundation.quickstart.model.token.RefreshToken;

import java.util.Optional;

public interface IUserDeviceRepository extends JpaRepository<UserDevice, Long> {

    @Override
    Optional<UserDevice> findById(Long id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserUseridAndDeviceId(Integer userId, String userDeviceId);
}
