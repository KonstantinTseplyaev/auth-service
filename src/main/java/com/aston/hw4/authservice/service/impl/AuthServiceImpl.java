package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.dto.UserAuthDto;
import com.aston.hw4.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenServiceImpl jwtTokenServiceImpl;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(UserAuthDto userAuthDto) {
        userAuthDto.setPassword(passwordEncoder.encode(userAuthDto.getPassword()));
        AppUser user = userRepository.findByEmailAndPassword(userAuthDto.getEmail(), userAuthDto.getPassword())
                .orElseThrow(() -> new RuntimeException("incorrect email or password"));
        String token = jwtTokenServiceImpl.generateToken(user);
        return ResponseEntity.ok(token);
    }
}
