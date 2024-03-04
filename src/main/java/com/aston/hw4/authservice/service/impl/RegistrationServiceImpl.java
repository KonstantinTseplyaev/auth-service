package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.exception.IncorrectUserCredentialException;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.UserRole;
import com.aston.hw4.authservice.model.dto.UserRegistrationDto;
import com.aston.hw4.authservice.service.RegistrationService;
import com.aston.hw4.authservice.util.BCryptEncoderUtil;
import com.aston.hw4.authservice.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final AppUserRepository userRepository;
    private final JwtTokenServiceImpl jwtTokenService;

    @Override
    public String registerUser(UserRegistrationDto userDto) {
        boolean emailIsExist = userRepository.existsByEmail(userDto.getEmail());

        if (emailIsExist) {
            throw new IncorrectUserCredentialException("user with this email already exists!");
        }

        userDto.setPassword(BCryptEncoderUtil.encode(userDto.getPassword()));

        AppUser user = MapperUtil.fromUserRegistrationDtoToAppUser(userDto);

        user.setRole(UserRole.ROLE_USER);

        AppUser persistenceUser = userRepository.save(user);
        return jwtTokenService.generateToken(persistenceUser);
    }
}
