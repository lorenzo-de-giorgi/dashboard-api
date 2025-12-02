package com.example.dashboardapi.controller;

import com.example.dashboardapi.dto.*;
import com.example.dashboardapi.model.User;
import com.example.dashboardapi.repository.UserRepository;
import com.example.dashboardapi.security.JwtUtil;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository repo;

    @Autowired
    PasswordEncoder encoder;

    // REGISTRA UTENTE
    @PostMapping("/register")
    public void register(@RequestBody LoginRequest req) {

        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setRole("ROLE_USER");

        repo.save(u);
    }

    // LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {

        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.username(),
                    req.password()));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtUtil.generateToken(req.username());

        return new AuthResponse(token);
    }

    // Debug: restituisce informazioni sull'Authentication corrente (se presente)
    @GetMapping("/whoami")
    public Map<String, Object> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return Map.of(
                    "authenticated", false,
                    "name", null,
                    "authorities", java.util.List.of()
            );
        }

        return Map.of(
                "authenticated", auth.isAuthenticated(),
                "name", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList())
        );
    }

    // GET di test: restituisce informazioni pubbliche dell'utente (senza password)
    // NOTE: user lookup moved to `/api/v1/users/{username}`. Keep register/login under /auth.
}
