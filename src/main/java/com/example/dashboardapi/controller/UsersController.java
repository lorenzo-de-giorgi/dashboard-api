package com.example.dashboardapi.controller;

import com.example.dashboardapi.dto.GetResponse;
import com.example.dashboardapi.dto.UserDto;
import com.example.dashboardapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @Autowired
    private UserRepository repo;

    @GetMapping("/{username}")
    public GetResponse<UserDto> getUser(@PathVariable String username) {
        UserDto dto = repo.findByUsername(username)
                .map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new GetResponse<>(true, dto, "OK");
    }
}
