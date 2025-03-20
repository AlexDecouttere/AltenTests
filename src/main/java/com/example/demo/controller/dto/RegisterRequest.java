package com.example.demo.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String firstname;
    private String email;
    private String password;
}