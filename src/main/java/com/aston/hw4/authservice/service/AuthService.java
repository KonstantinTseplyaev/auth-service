package com.aston.hw4.authservice.service;

import com.aston.hw4.authservice.model.dto.UserAuthDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(UserAuthDto userAuthDto);
}
