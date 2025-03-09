package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.service.JwtUtils;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {


        try {
            boolean isAuthenticated = userService.verify(user);
            if (isAuthenticated) {
                return ResponseEntity.status(HttpStatus.OK).body(jwtUtils.generateToken(user.getUsername()));
            }else{
                return ResponseEntity.status(401).body("Invalid credentials");

            }
        }catch (Exception e){

           return    ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
