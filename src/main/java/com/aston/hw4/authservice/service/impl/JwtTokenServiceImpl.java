package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.UserRole;
import com.aston.hw4.authservice.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Duration lifetime;
    private final AppUserRepository userRepository;

    @Override
    public String generateToken(AppUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole());
        claims.put("email", user.getEmail());

        Date issureDate = new Date();
        Date expiredDate = new Date(issureDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issureDate)
                .setExpiration(expiredDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isValidToken(String jwtToken) {
        try {
            Date expirationToken = extractExpiration(jwtToken);
            if (expirationToken.before(new Date())) {
                return false;
            }

            long id = extractUserId(jwtToken);
            AppUser user = userRepository.findById(id).orElseThrow();
            String userEmail = user.getEmail();
            UserRole userRole = user.getRole();

            if (userEmail.equals(extractEmail(jwtToken)) && userRole == (extractRole(jwtToken))) {
                return true;
            }

        } catch (JwtException exp) {
            return false;
        }
        return false;
    }

    private int extractUserId(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return (int) claims.get("id");
    }

    private UserRole extractRole(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return UserRole.valueOf((String) claims.get("role"));
    }

    private String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("email");
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
