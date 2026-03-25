package com.example.bankcards.repository;

import com.example.bankcards.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Crud Repository for {@link RefreshToken}.
 */

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(UUID token);

    Iterable<RefreshToken> findByUserId(UUID userId);

}
