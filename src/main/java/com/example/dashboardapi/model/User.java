package com.example.dashboardapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    private String role;
}
