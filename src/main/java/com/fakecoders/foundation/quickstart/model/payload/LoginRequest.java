package com.fakecoders.foundation.quickstart.model.payload;

import com.fakecoders.foundation.quickstart.validation.annotation.NullOrNotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Login Request", description = "The login request payload")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@NullOrNotBlank(message = "Login Username can be null but not blank")
	@Schema(name = "Registered username", allowableValues = "NonEmpty String")
	private String username;

	// @NullOrNotBlank(message = "Login Email can be null but not blank")
	@Schema(name = "User registered email", required = true, allowableValues = "NonEmpty String")
	private String email;

	@NotNull(message = "Login password cannot be blank")
	@Schema(name = "Valid user password", required = true, allowableValues = "NonEmpty String")
	private String password;

	@Valid
	@NotNull(message = "Device info cannot be null")
	@Schema(name = "Device info", required = true, type = "object", allowableValues = "A valid " +
			"deviceInfo object")
	private DeviceInfo deviceInfo;


}
