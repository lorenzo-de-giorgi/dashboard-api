package com.example.dashboardapi.controller;

import com.example.dashboardapi.dto.GetResponse;
import com.example.dashboardapi.dto.UserDto;
import com.example.dashboardapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @Autowired
    private UserRepository repo;

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    @GetMapping("/{username}")
    public GetResponse<UserDto> getUser(@PathVariable String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // debug logging to help diagnose 403 cases
        if (auth != null) {
            log.info("Request for user='{}' by authentication.name='{}' authorities={}", username, auth.getName(), auth.getAuthorities());
        } else {
            log.info("Request for user='{}' with no authentication present", username);
        }

        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        if (!isAdmin && !username.equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        UserDto dto = repo.findByUsername(username)
                .map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new GetResponse<>(true, dto, "OK");
    }
}
