package com.aston.hw4.authservice.service.impl;

import com.aston.hw4.authservice.dao.AppUserRepository;
import com.aston.hw4.authservice.model.AppUser;
import com.aston.hw4.authservice.model.Role;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        claims.put("id", user.getId());
        claims.put("roles", roles);

        Date issureDate = new Date();
        Date expiredDate = new Date(issureDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .setSubject(user.getEmail())
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
            Set<Role> userRoles = user.getRoles();

            if (userEmail.equals(extractEmail(jwtToken))) {
                List<String> jwtRoles = extractRoles(jwtToken);
                for (Role role : userRoles) {
                    if (!jwtRoles.contains(role.getName())) {
                        return false;
                    }
                }
            }
        } catch (JwtException exp) {
            return false;
        }
        return true;
    }

    private int extractUserId(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return (int) claims.get("id");
    }

    private List<String> extractRoles(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        List<String> roles = claims.get("roles", List.class);
        return roles;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
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
