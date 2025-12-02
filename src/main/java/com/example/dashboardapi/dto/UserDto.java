package com.example.dashboardapi.dto;

import java.time.LocalDate;

public record UserDto(Long id, String email, String password, String name, String surname, LocalDate date_of_birth, String gender, String phone_number, String address, String permission) {

}
