package com.mockserver.service;

import com.mockserver.exception.RequestIsBadException;
import com.mockserver.exception.ResourceNotFoundException;
import com.mockserver.model.entity.RefreshToken;
import com.mockserver.model.response.TokenResponse;
import com.mockserver.model.shared.UserPrincipal;
import com.mockserver.repository.relational.UserRepo;
import com.mockserver.security.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse createTokenResponse(UserPrincipal userPrincipal) {
        String accessToken = tokenProvider.createToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());
        
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken.getToken())
            .tokenType("Bearer")
            .expiresIn(tokenProvider.getJwtExpiration() / 1000)
            .build();
    }
    
    public TokenResponse refreshToken(String refreshToken) {
        return refreshTokenService.getByToken(refreshToken)
            .map(token -> {
                if (refreshTokenService.verifyExpiration(token)) {
                    UserPrincipal userPrincipal = UserPrincipal.create(token.getUser());
                    String accessToken = tokenProvider.createToken(userPrincipal);
                    
                    return TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(token.getToken())
                        .tokenType("Bearer")
                        .expiresIn(tokenProvider.getJwtExpiration() / 1000)
                        .build();
                }
                throw new RequestIsBadException("Refresh token expired");
            })
            .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
    }
}