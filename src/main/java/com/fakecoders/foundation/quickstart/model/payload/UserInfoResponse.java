package com.fakecoders.foundation.quickstart.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
	
		private String email;
		
		private String username;
		
		private String firstname;
		
		private String lastname;
		
		private String mobile;
}
