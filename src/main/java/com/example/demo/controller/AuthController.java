package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.service.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {


        try {
            Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()){
                return ResponseEntity.status(HttpStatus.OK).body(jwtUtils.generateToken(authentication));
            }
            else{
                return ResponseEntity.status(401).body("Invalid credentials");

            }
        }catch (Exception e){
           return    ResponseEntity.status(401).body(e.getMessage());
        }
    }


}
