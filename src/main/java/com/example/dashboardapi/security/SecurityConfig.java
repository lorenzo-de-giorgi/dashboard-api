package com.example.dashboardapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(
                new JwtFilter(jwtUtil, uds),
                UsernamePasswordAuthenticationFilter.class
        );

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

    // Aggiunto UserDetailsService in memoria per far partire subito l'app
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password(encoder().encode("password"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
