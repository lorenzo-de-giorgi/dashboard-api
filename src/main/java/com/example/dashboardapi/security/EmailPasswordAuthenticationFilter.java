package com.example.dashboardapi.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Authentication filter that reads credentials from `email` / `password` fields
 * instead of the default `username` parameter.
 */
public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        // change the expected username parameter to 'email'
        super.setUsernameParameter("email");
    }

    // keep the default behavior otherwise
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(getUsernameParameter());
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(getPasswordParameter());
    }
}
