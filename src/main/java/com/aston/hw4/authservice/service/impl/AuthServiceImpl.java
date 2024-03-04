package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.exception.IncorrectUserCredentialException;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.dto.UserAuthDto;
import com.aston.hw4.authservice.service.AuthService;
import com.aston.hw4.authservice.util.BCryptEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenServiceImpl jwtTokenServiceImpl;
    private final AppUserRepository userRepository;

    @Override
    public String login(UserAuthDto userAuthDto) {
        userAuthDto.setPassword(BCryptEncoderUtil.encode(userAuthDto.getPassword()));
        AppUser user = userRepository.findByEmailAndPassword(userAuthDto.getEmail(), userAuthDto.getPassword())
                .orElseThrow(() -> new IncorrectUserCredentialException("incorrect email or password"));
        return jwtTokenServiceImpl.generateToken(user);
    }
}
