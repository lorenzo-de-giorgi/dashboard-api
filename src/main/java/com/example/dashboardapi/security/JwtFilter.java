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

                String username =
                        jwtUtil.extractUsername(token);

                UserDetails details =
                        uds.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                details,
                                null,
                                details.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
