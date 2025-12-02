package com.example.dashboardapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name="users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String name;
    
    @Column(nullable=false)
    private String surname;

    @Column(nullable=false)
    private LocalDate date_of_birth;

    @Column(nullable=false, length = 1)
    private String gender;

    @Column(nullable=false)
    private String phone_number;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String permission;
}
