package com.taskmanager.auth;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.taskmanager.controllers.LastWord;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Note that the user will login and then according to their grantedAuthority.getAuthority() to /admin/home or /user/home 
public class AuthenticationAccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	Logger logger = LoggerFactory.getLogger(AuthenticationAccessHandler.class.getName());

	// when the login is a success then grant "ROLE_ADMIN" and land on the
	// /admin/home or /user/home page
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		LastWord lastWord = new LastWord(getClass().getName());
		System.out.println("Class = " + lastWord.getLastWord());
		logger.trace("ENTERED……………………………………onAuthenticationSuccess()");

		// if the granted authority is equal to"ROLE_ADMIN"
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

		if (isAdmin) {
			setDefaultTargetUrl("/admin/home");
			logger.trace("EXITED……………………………………onAuthenticationSuccess()---> /admin/home");
		} else {
			logger.trace("EXITED……………………………………onAuthenticationSuccess()--> /user/home");
			setDefaultTargetUrl("/user/home");
		}

		// setDefaultTargetUrl();
		super.onAuthenticationSuccess(request, response, authentication);

	}
}
