package com.example.dashboardapi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * A small convenience subclass to make the code explicit when the principal is an email.
 * It simply reuses {@link UsernamePasswordAuthenticationToken} behavior.
 */
public class EmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public EmailPasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public EmailPasswordAuthenticationToken(Object principal, Object credentials,
                                            Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
