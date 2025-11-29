package com.example.dashboardapi.dto;

public record LoginRequest(
        String username,
        String password
) {}
