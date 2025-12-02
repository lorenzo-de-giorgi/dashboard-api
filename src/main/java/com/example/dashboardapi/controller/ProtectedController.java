package com.example.dashboardapi.controller;

import com.example.dashboardapi.dto.GetResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
public class ProtectedController {

    @GetMapping("/test")
    public GetResponse<String> testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null) ? auth.getName() : null;
        if (email == null) {
            return new GetResponse<>(false, null, "Unauthenticated");
        }
        return new GetResponse<>(true, "hello, " + email, "Authenticated");
    }
}
