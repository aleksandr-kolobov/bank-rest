package com.example.bankcards.security;

import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.exception.RefreshTokenException;
import com.example.bankcards.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for {@link RefreshToken}
 */

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByRefreshToken(UUID token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UUID userId) {
        var refreshToken = RefreshToken.builder()
                .userId(userId)
                .expireDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                .token(UUID.randomUUID())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Срок хранения токена обновления истек");
        }
        return token;
    }

    public void deleteRefreshToken(UUID userId) {
        refreshTokenRepository.findByUserId(userId)
                .forEach(t -> refreshTokenRepository.deleteById(t.getToken()));
    }

}
