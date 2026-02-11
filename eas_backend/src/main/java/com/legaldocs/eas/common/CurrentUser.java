package com.legaldocs.eas.common;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {
	
	public static UUID id() {
        Object principal =
            SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UUID uuid) {
            return uuid;
        }

        throw new IllegalStateException("User not authenticated");
    }
	
}
