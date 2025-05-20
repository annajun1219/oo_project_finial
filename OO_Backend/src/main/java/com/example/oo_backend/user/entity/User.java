package com.example.oo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private LocalDate birth;

    private String profileImage;
}