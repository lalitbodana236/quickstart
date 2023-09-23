package com.fakecoders.foundation.quickstart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fakecoders.foundation.quickstart.exception.TokenRefreshException;
import com.fakecoders.foundation.quickstart.model.UserDevice;
import com.fakecoders.foundation.quickstart.model.payload.DeviceInfo;
import com.fakecoders.foundation.quickstart.model.token.RefreshToken;
import com.fakecoders.foundation.quickstart.repository.IUserDeviceRepository;

import java.util.Optional;

@Service
public class UserDeviceService {

    private final IUserDeviceRepository userDeviceRepository;

    @Autowired
    public UserDeviceService(IUserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    /**
     * Find the user device info by user id
     */
    public Optional<UserDevice> findDeviceByUserId(Integer userId, String deviceId) {
        return userDeviceRepository.findByUserUseridAndDeviceId(userId, deviceId);
    }

    /**
     * Find the user device info by refresh token
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Creates a new user device and set the user to the current device
     */
    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setNotificationToken(deviceInfo.getNotificationToken());
        userDevice.setIsRefreshActive(true);
        return userDevice;
    }

    /**
     * Check whether the user device corresponding to the token has refresh enabled and
     * throw appropriate errors to the client
     */
    void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "No device found for the matching token. Please login again"));

        if (!userDevice.getIsRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh blocked for the device. Please login through a different device");
        }
    }
}
