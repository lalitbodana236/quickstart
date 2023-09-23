package com.fakecoders.foundation.quickstart.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;


import static com.fakecoders.foundation.quickstart.model.Permission.ADMIN_CREATE;
import static com.fakecoders.foundation.quickstart.model.Permission.ADMIN_DELETE;
import static com.fakecoders.foundation.quickstart.model.Permission.ADMIN_READ;
import static com.fakecoders.foundation.quickstart.model.Permission.ADMIN_UPDATE;
import static com.fakecoders.foundation.quickstart.model.Permission.MANAGER_CREATE;
import static com.fakecoders.foundation.quickstart.model.Permission.MANAGER_DELETE;
import static com.fakecoders.foundation.quickstart.model.Permission.MANAGER_READ;
import static com.fakecoders.foundation.quickstart.model.Permission.MANAGER_UPDATE;

import static com.fakecoders.foundation.quickstart.model.Role.ADMIN;
import static com.fakecoders.foundation.quickstart.model.Role.MANAGER;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	private final JwtAuthenticationEntryPoint jwtEntryPoint;

	// private final LogoutHandler logoutHandler;


	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf()
		.disable()
		.exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
		.and()
		.authorizeHttpRequests()
		.requestMatchers(
				"/api/auth/**",
				"/api/v1/auth/**"
				).permitAll()
		.requestMatchers(
				"/v3/api-docs/**", 
				"/swagger-ui/**",
				"/swagger-ui.html",
				"/configuration/ui", 
				"/swagger-resources/**", 
				"/configuration/**",
				"/swagger-ui/**",
				"/webjars/**"
				).permitAll()


		.requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())


		.requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
		.requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
		.requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
		.requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())


		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		//.logout()
		//.logoutUrl("/api/v1/auth/logout")
		// .addLogoutHandler(logoutHandler)
		//.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
		;

		return http.build();
	}


}