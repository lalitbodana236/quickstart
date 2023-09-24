package com.fakecoders.foundation.quickstart.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fakecoders.foundation.quickstart.custom.model.CustomUserDetails;
import com.fakecoders.foundation.quickstart.exception.PasswordResetLinkException;
import com.fakecoders.foundation.quickstart.exception.ResourceAlreadyInUseException;
import com.fakecoders.foundation.quickstart.exception.ResourceNotFoundException;
import com.fakecoders.foundation.quickstart.exception.TokenRefreshException;
import com.fakecoders.foundation.quickstart.exception.UpdatePasswordException;
import com.fakecoders.foundation.quickstart.model.PasswordResetToken;
import com.fakecoders.foundation.quickstart.model.TokenStatus;
import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.model.UserDevice;
import com.fakecoders.foundation.quickstart.model.payload.LoginRequest;
import com.fakecoders.foundation.quickstart.model.payload.PasswordResetLinkRequest;
import com.fakecoders.foundation.quickstart.model.payload.PasswordResetRequest;
import com.fakecoders.foundation.quickstart.model.payload.RegistrationRequest;
import com.fakecoders.foundation.quickstart.model.payload.TokenRefreshRequest;
import com.fakecoders.foundation.quickstart.model.payload.UpdatePasswordRequest;
import com.fakecoders.foundation.quickstart.model.token.EmailVerificationToken;
import com.fakecoders.foundation.quickstart.model.token.RefreshToken;
import com.fakecoders.foundation.quickstart.security.config.JwtService;
import com.fakecoders.foundation.quickstart.service.IUserService;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final IUserService userService;
    
    private final JwtService tokenProvider;
    
    private final RefreshTokenService refreshTokenService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    
    private final EmailVerificationTokenService emailVerificationTokenService;
    
    private final UserDeviceService userDeviceService;
    
    private final PasswordResetTokenService passwordResetService;

    @Autowired
    public AuthService(IUserService userService, JwtService tokenProvider, RefreshTokenService refreshTokenService, PasswordEncoder	 passwordEncoder, AuthenticationManager authenticationManager, EmailVerificationTokenService emailVerificationTokenService, UserDeviceService userDeviceService, PasswordResetTokenService passwordResetService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.userDeviceService = userDeviceService;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            logger.error("Email already exists: " + newRegistrationRequestEmail);
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
        }
        logger.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
        User newUser = userService.createUser(newRegistrationRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    public Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    /**
     * Authenticate user and log them in given a loginRequest
     */
    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
    	
    	logger.info("inside JwtAuthenticationFilter sevice");
    	Optional<User> user = userService.findByEmail(loginRequest.getEmail());
    	logger.info(" {} , {} ",passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword()),loginRequest.getPassword());
    	//authenticationManager.authenticate(null)
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword())));
    }

    /**
     * Confirms the user verification based on the token expiry and mark the user as active.
     * If user is already verified, save the unnecessary database calls.
     */
    public Optional<User> confirmEmailRegistration(String emailToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(emailToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification", emailToken));

        User registeredUser = emailVerificationToken.getUser();
        if (registeredUser.getIsEmailVerified()) {
            logger.info("User [" + emailToken + "] already registered.");
            return Optional.of(registeredUser);
        }

        emailVerificationTokenService.verifyExpiration(emailVerificationToken);
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_CONFIRMED);
        emailVerificationTokenService.save(emailVerificationToken);

        registeredUser.setIsEmailVerified(true);
        userService.save(registeredUser);
        return Optional.of(registeredUser);
    }

    /**
     * Attempt to regenerate a new email verification token given a valid
     * previous expired token. If the previous token is valid, increase its expiry
     * else update the token value and add a new expiration.
     */
    public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Existing email verification", existingToken));

        if (emailVerificationToken.getUser().getIsEmailVerified()) {
            return Optional.empty();
        }
        return Optional.ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
    }

    /**
     * Validates the password of the current logged in user with the given password
     */
    private Boolean currentPasswordMatches(User currentUser, String password) {
        return passwordEncoder.matches(password, currentUser.getPassword());
    }

    /**
     * Updates the password of the current logged in user
     */
    public Optional<User> updatePassword(CustomUserDetails customUserDetails,
                                         UpdatePasswordRequest updatePasswordRequest) {
        String email = customUserDetails.getUsername();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new UpdatePasswordException(email, "No matching user found"));

        if (!currentPasswordMatches(currentUser, updatePasswordRequest.getOldPassword())) {
            logger.info("Current password is invalid for [" + currentUser.getPassword() + "]");
            throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
        }
        String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        currentUser.setPassword(newPassword);
        userService.save(currentUser);
        return Optional.of(currentUser);
    }

    /**
     * Generates a JWT token for the validated client
     */
    public String generateToken(CustomUserDetails customUserDetails) {
        return tokenProvider.generateToken(customUserDetails);
    }

    /**
     * Generates a JWT token for the validated client by userId
     */
    private String generateTokenFromUserId(Integer userId) {
    	Optional<User> user =userService.findByUserId(userId);
        return generateToken(new CustomUserDetails(user.get()));
    }

    /**
     * Creates and persists the refresh token for the user device. If device exists
     * already, we recreate the refresh token. Unused devices with expired tokens
     * should be cleaned externally.
     */
    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest) {
    	CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        String deviceId = loginRequest.getDeviceInfo().getDeviceId();
        userDeviceService.findDeviceByUserId(currentUser.getUser().getUserid(), deviceId)
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);

        UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.getDeviceInfo());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setUser(currentUser.getUser());
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    /**
     * Refresh the expired jwt token using a refresh token and device info. The
     * * refresh token is mapped to a specific device and if it is unexpired, can help
     * * generate a new jwt. If the refresh token is inactive for a device or it is expired,
     * * throw appropriate errors.
     */
    public Optional<String> refreshJwtToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userDeviceService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserDevice)
                .map(UserDevice::getUser)
                .map(CustomUserDetails::new)
                .map(this::generateToken))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Missing refresh token in database.Please login again"));
    }

    /**
     * Generates a password reset token from the given reset request
     */
    public Optional<PasswordResetToken> generatePasswordResetToken(PasswordResetLinkRequest passwordResetLinkRequest) {
        String email = passwordResetLinkRequest.getEmail();
        logger.info("generatePasswordResetToken email {} ",email);
        return userService.findByEmail(email)
                .map(passwordResetService::createToken)
                .orElseThrow(() -> new PasswordResetLinkException(email, "No matching user found for the given request"));
    }

    /**
     * Reset a password given a reset request and return the updated user
     * The reset token must match the email for the user and cannot be used again
     * Since a user could have requested password multiple times, multiple tokens
     * would be generated. Hence, we need to invalidate all the existing password
     * reset tokens prior to changing the user password.
     */
    public Optional<User> resetPassword(PasswordResetRequest request) {
        PasswordResetToken token = passwordResetService.getValidToken(request);
        final String encodedPassword = passwordEncoder.encode(request.getConfirmPassword());

        return Optional.of(token)
                .map(passwordResetService::claimToken)
                .map(PasswordResetToken::getUser)
                .map(user -> {
                    user.setPassword(encodedPassword);
                    userService.save(user);
                    return user;
                });
    }
}
