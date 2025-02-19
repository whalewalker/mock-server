package com.mockserver.service;

import com.mockserver.exception.RequestIsBadException;
import com.mockserver.model.entity.RefreshToken;
import com.mockserver.model.entity.User;
import com.mockserver.repository.relational.RefreshTokenRepo;
import com.mockserver.repository.relational.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh-expiration}")
    private  Long refreshTokenDurationMs;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepo.getById(userId);
        RefreshToken existingToken = refreshTokenRepo.getByUser(user);

        if (existingToken != null) {
            refreshTokenRepo.delete(existingToken.getId());
        }

        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000))
            .build();

        return refreshTokenRepo.create(refreshToken);
    }

    public boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepo.delete(token.getId());
            return false;
        }
        return true;
    }

    @Transactional
    public void deleteByUserId(UUID userId) {
        User user = userRepo.getById(userId);
        refreshTokenRepo.deleteByUser(user);
    }

    public Optional<RefreshToken> getByToken(String refreshToken) {
        return Optional.of(refreshTokenRepo.getByToken(refreshToken));
    }
}