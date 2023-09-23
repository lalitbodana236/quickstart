package com.fakecoders.foundation.quickstart.security.config;

import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final JwtService jwtService;

	private final UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenValidator jwtTokenValidator;

	@Value("${app.jwt.header}")
	private String tokenRequestHeader;

	@Value("${app.jwt.header.prefix}")
	private String tokenRequestHeaderPrefix;


	/**
	 * Filter the incoming request for a valid token in the request header
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			logger.info("inside JwtAuthenticationFilter header {} , request {} ",request, request.getHeaderNames().toString());
			logger.info("inside JwtAuthenticationFilter");
			if (request.getServletPath().contains("/api/v1/auth")) {
				filterChain.doFilter(request, response);
				return;
			}
			
			logger.info("inside JwtAuthenticationFilter header {} ",request.getHeader("Content-Type"));
			final String authHeader = request.getHeader("Authorization");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
			    String headerName = headerNames.nextElement();
			    String headerValue = request.getHeader(headerName);
			    System.out.println(headerName + ": " + headerValue);
			}
			
			final String jwt;
			final String userEmail;
			if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			logger.info("inside JwtAuthenticationFilter authHeader {}",authHeader);
			jwt = authHeader.substring(7);
			logger.info("inside JwtAuthenticationFilter jwt {}",jwt);
			userEmail = jwtService.extractUsername(jwt);
			logger.info("inside JwtAuthenticationFilter userEmail {}",userEmail);
			if (StringUtils.hasText(jwt) && jwtTokenValidator.validateToken(jwt)) {
				logger.info("inside JwtAuthenticationFilter in");
				if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					logger.info("inside JwtAuthenticationFilter in in");
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
					if (jwtService.isTokenValid(jwt, userDetails)) {
						logger.info("inside JwtAuthenticationFilter in in in");
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities()
								);
						authToken.setDetails(
								new WebAuthenticationDetailsSource().buildDetails(request)
								);
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			}



		} catch (Exception ex) {
			ex.printStackTrace();
			//log.error("Failed to set user authentication in security context: ", ex);
			throw ex;
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Extract the token from the Authorization request header
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(tokenRequestHeader);
		if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenRequestHeaderPrefix)) {
			logger.info("Extracted Token: " + bearerToken);
			return bearerToken.replace(tokenRequestHeaderPrefix, "");
		}
		return null;
	}
}
