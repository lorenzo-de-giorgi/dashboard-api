package com.example.dashboardapi.dto;

public record LoginRequest(
        String email,
        String password
) {}
