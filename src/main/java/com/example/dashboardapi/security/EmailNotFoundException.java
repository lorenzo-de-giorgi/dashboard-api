package com.example.dashboardapi.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Semantic alias for UsernameNotFoundException to indicate the lookup was by email.
 */
public class EmailNotFoundException extends UsernameNotFoundException {
    public EmailNotFoundException(String msg) {
        super(msg);
    }

    public EmailNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
