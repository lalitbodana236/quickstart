package com.fakecoders.foundation.quickstart.model.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
		private String accessToken;

	    private String refreshToken;

	    private String tokenType;

	    private Long expiryDuration;
	    
	    private UserInfoResponse userInfo;
	    
	    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long expiryDuration) {
	        this.accessToken = accessToken;
	        this.refreshToken = refreshToken;
	        this.expiryDuration = expiryDuration;
	        tokenType = "Bearer ";
	    }
	    
	    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long expiryDuration,UserInfoResponse userInfo) {
	        this.accessToken = accessToken;
	        this.refreshToken = refreshToken;
	        this.expiryDuration = expiryDuration;
	        this.userInfo=userInfo;
	        tokenType = "Bearer ";
	    }
}
