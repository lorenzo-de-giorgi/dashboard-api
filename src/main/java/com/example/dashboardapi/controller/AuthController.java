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

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()));

        String token =
                jwtUtil.generateToken(req.username());

        return new AuthResponse(token);
    }

    // GET di test: restituisce informazioni pubbliche dell'utente (senza password)
    @GetMapping("/user/{username}")
    public com.example.dashboardapi.dto.UserDto getUser(@PathVariable String username) {
        return repo.findByUsername(username)
                .map(u -> new com.example.dashboardapi.dto.UserDto(u.getId(), u.getUsername(), u.getRole()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
