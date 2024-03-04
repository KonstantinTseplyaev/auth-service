package com.aston.hw4.authservice.service;

import com.aston.hw4.authservice.model.dto.UserRegistrationDto;

public interface RegistrationService {
    String registerUser(UserRegistrationDto user);
}
