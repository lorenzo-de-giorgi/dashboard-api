package com.example.dashboardapi.security;

import com.example.dashboardapi.model.User;
import com.example.dashboardapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public DatabaseUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loadUserByEmail(email);
    }

    /**
     * Explicit email-based loader. Keeps compatibility with Spring's
     * {@code UserDetailsService.loadUserByUsername} while making intent clear.
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User u = repo.findByEmail(email)
            .orElseThrow(() -> new com.example.dashboardapi.security.EmailNotFoundException("User not found"));

        return buildUserDetails(u);
    }

    private UserDetails buildUserDetails(User u) {
        return org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
            .password(u.getPassword())
            .authorities(u.getPermission())
            .build();
    }
}
