package com.aston.hw4.authservice.controller;

import com.aston.hw4.authservice.model.dto.UserAuthDto;
import com.aston.hw4.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //public endpoint
    @PostMapping
    public ResponseEntity<String> login(@RequestBody UserAuthDto user) {
        log.debug("login user with data: {}", user);
        return ResponseEntity.ok(authService.login(user));
    }
}
