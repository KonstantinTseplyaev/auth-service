package com.aston.hw4.authservice.service;

import com.aston.hw4.authservice.model.dto.UserAuthDto;

public interface AuthService {
    String login(UserAuthDto userAuthDto);
}
