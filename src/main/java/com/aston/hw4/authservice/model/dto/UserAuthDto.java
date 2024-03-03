package com.aston.hw4.authservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserAuthDto {
    private String email;
    @Setter
    private String password;
}
