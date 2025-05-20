package com.example.oo_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String password2;
    private String phone;
    private LocalDate birth;
}

