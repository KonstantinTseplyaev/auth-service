package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.dao.RoleRepository;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.Role;
import com.aston.hw4.authservice.model.dto.UserRegistrationDto;
import com.aston.hw4.authservice.service.RegistrationService;
import com.aston.hw4.authservice.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final AppUserRepository userRepository;
    private final JwtTokenServiceImpl jwtTokenService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> registerUser(UserRegistrationDto userDto) {
        boolean emailIsExist = userRepository.existsByEmail(userDto.getEmail());

        if (emailIsExist) {
            throw new RuntimeException("user with this email already exists!");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        AppUser user = MapperUtil.fromUserRegistrationDtoToAppUser(userDto);

        user.setRoles(getDefaultRoles());

        AppUser persistenceUser = userRepository.save(user);
        String token = jwtTokenService.generateToken(persistenceUser);
        return ResponseEntity.ok(token);
    }

    private Set<Role> getDefaultRoles() {
        Role roleUser = roleRepository.findByName("ROLE_USER").get();
        return Set.of(roleUser);
    }
}
