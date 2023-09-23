package com.fakecoders.foundation.quickstart.custom.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.catalina.CredentialHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fakecoders.foundation.quickstart.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

	private User user;

	public CustomUserDetails(final User user) {
		this.user=user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//SimpleGrantedAuthority authority =new SimpleGrantedAuthority(user.getRole());
		//return Arrays.asList(authority);
		//  return getRoles().stream()
		//  .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
		// .collect(Collectors.toList());
		return List.of(new SimpleGrantedAuthority(user.getRole().name()));
	}	

	public User getUser() {
		return user;
	}

	public Integer getUserid() {
		return user.getUserid();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(user.getUserid());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CustomUserDetails that = (CustomUserDetails) obj;
		return Objects.equals(user.getUserid(), that.user.getUserid());
	}



}
