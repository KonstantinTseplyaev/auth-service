package com.aston.hw4.authservice.util;

import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.dto.UserRegistrationDto;

public class MapperUtil {

    public static AppUser fromUserRegistrationDtoToAppUser(UserRegistrationDto userDto) {
        return AppUser.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
}
