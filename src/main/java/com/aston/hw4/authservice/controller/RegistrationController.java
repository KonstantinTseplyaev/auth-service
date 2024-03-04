package com.aston.hw4.authservice.controller;

import com.aston.hw4.authservice.model.dto.UserRegistrationDto;
import com.aston.hw4.authservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    //public endpoint
    @PostMapping()
    public ResponseEntity<String> registration(@RequestBody UserRegistrationDto user) {
        log.debug("registration user with data: {}", user);
        return ResponseEntity.ok(registrationService.registerUser(user));
    }
}
