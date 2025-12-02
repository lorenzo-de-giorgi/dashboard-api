package com.example.dashboardapi.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;

    public JwtFilter(JwtUtil jwtUtil,
                     UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String header =
                request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);

                UserDetails details;
                if (uds instanceof com.example.dashboardapi.security.DatabaseUserDetailsService duds) {
                    details = duds.loadUserByEmail(email);
                } else {
                    details = uds.loadUserByUsername(email);
                }

                EmailPasswordAuthenticationToken auth = new EmailPasswordAuthenticationToken(
                        details,
                        null,
                        details.getAuthorities()
                );

                // populate web details (useful for logging/audit)
                auth.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
