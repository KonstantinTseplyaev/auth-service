package com.aston.hw4.authservice.service;

import com.aston.hw4.authservice.model.dto.UserRegistrationDto;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {
    ResponseEntity<?> registerUser(UserRegistrationDto user);
}
