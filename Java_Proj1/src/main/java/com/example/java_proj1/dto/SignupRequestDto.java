package com.example.java_proj1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignupRequestDto {
    private String username;
    private String password;
    private String major;
    private String grade;
}
