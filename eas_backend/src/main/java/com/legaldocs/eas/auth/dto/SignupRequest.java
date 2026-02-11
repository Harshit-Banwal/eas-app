package com.legaldocs.eas.auth.dto;

public record SignupRequest (
		String email,
	    String password
	) {}
