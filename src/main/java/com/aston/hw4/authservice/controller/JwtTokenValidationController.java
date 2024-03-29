package com.aston.hw4.authservice.controller;

import com.aston.hw4.authservice.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/valid")
@RequiredArgsConstructor
public class JwtTokenValidationController {
    private final JwtTokenService jwtTokenService;

    //endpoint for gateway service
    @GetMapping
    public boolean checkToken(@RequestParam("token") String bearerToken) {
        log.debug("check token");
        return jwtTokenService.isValidToken(bearerToken);
    }
}
