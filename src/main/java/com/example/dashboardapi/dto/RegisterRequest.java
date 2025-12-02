package com.example.dashboardapi.dto;

public record RegisterRequest(
        String email,
        String password,
        String name,
        String surname,
        java.time.LocalDate date_of_birth,
        String gender,
        String phone_number,
        String address,
        String permission
) {}
