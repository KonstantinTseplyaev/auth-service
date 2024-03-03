package com.aston.hw4.authservice.service;

import com.aston.hw4.authservice.model.AppUser;

public interface JwtTokenService {
    String generateToken(AppUser user);

    boolean isValidToken(String token);
}
