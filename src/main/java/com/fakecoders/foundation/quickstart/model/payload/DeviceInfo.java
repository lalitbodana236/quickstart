package com.fakecoders.foundation.quickstart.model.payload;

import com.fakecoders.foundation.quickstart.validation.annotation.NullOrNotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceInfo {

    @NotBlank(message = "Device id cannot be blank")
    @Schema(name = "Device Id", required = true, type = "string", allowableValues = "Non empty string")
    private String deviceId;

    @NotNull(message = "Device type cannot be null")
    @Schema(name = "Device type Android/iOS", required = true, type = "string", allowableValues = "Non empty string")
    private String deviceType;

    @NullOrNotBlank(message = "Device notification token can be null but not blank")
    @Schema(name = "Device notification id", type = "string", allowableValues = "Non empty string")
    private String notificationToken;

   
}
