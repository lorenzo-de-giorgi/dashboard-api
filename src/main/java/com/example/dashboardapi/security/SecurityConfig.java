package com.example.dashboardapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import com.example.dashboardapi.security.EmailPasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtUtil jwtUtil,
            UserDetailsService uds
    ) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(
                sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // ensure unauthenticated requests receive a 401 JSON response
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(new RestAuthenticationEntryPoint()));

        http.authorizeHttpRequests(auth -> auth
                // allow unauthenticated access to auth endpoints (login/register)
                .requestMatchers("/auth/**").permitAll()
                // allow an explicit login path under /api/v1 if ever used
                .requestMatchers("/api/v1/login").permitAll()
                // require authentication for all other /api/v1 endpoints
                .requestMatchers("/api/v1/**").authenticated()
                // other endpoints remain permitted
                .anyRequest().permitAll()
        );

        http.addFilterBefore(
                new JwtFilter(jwtUtil, uds),
                EmailPasswordAuthenticationFilter.class
        );

        // Note: authentication is performed in `AuthController` (login endpoint),
        // so we do not register a stateful Username/Email form filter here.

        return http.build();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(
            AuthenticationConfiguration cfg
    ) throws Exception {
        return cfg.getAuthenticationManager();
    }

}
