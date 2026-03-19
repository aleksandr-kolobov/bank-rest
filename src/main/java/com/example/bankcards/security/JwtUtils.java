package com.example.bankcards.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static com.example.bankcards.config.ApplicationConstant.*;
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.accessTokenExpiration}")
    private Duration tokenExpiration;

    @Value("${app.jwt.secretKey}")
    private String secretKey;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateJwtToken(String userEmail, UUID userId, String roles) {
        return Jwts.builder()
                .subject(userEmail)
                .claim(HEADER_KEY_USER_ID, userId.toString())
                .claim(HEADER_KEY_USER_ROLES, roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + tokenExpiration.toMillis()))
                .signWith(getSignKey())
                .compact();
    }

    public Claims validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("JwtsParserException: {}", e.getMessage());
        }
        return null;
    }

}
