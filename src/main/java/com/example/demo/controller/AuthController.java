package com.example.demo.controller;

import com.example.demo.Service.UserService;
import com.example.demo.conf.JwtUtil;
import com.example.demo.controller.dto.AuthRequest;
import com.example.demo.controller.dto.LoginResponse;
import com.example.demo.controller.dto.RegisterRequest;
import com.example.demo.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil,UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }



    @PostMapping("/accounts")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User newUser = userService.registerUser(request);
            return ResponseEntity.ok("User registered successfully with email: " + newUser.getEmail());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String token = userService.login(request);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

