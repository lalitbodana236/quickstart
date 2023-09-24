package com.fakecoders.foundation.quickstart.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Registration Request", description = "The registration request payload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

   // @NullOrNotBlank(message = "Registration username can be null but not blank")
    @Schema(name = "A valid username", allowableValues = "NonEmpty String")
    private String username;

  //  @NullOrNotBlank(message = "Registration email can be null but not blank")
    @Schema(name = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Registration password cannot be null")
    @Schema(name = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;
    
    @Schema(name = "A valid firstname", allowableValues = "NonEmpty String")
    private String firstname;
    
    @Schema(name = "A valid lastname", allowableValues = "NonEmpty String")
    private String lastname;
    
    @Schema(name = "A valid mobile")
    private String mobile;

 //   @NotNull(message = "Specify whether the user has to be registered as an admin or not")
   // @Schema(name = "Flag denoting whether the user is an admin or not", required = true,
   //         type = "boolean", allowableValues = "true, false")
  //  private Boolean registerAsAdmin;

   
}
