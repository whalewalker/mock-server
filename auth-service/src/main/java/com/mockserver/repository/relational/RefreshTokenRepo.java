package com.mockserver.repository.relational;

import com.mockserver.exception.ResourceNotFoundException;
import com.mockserver.model.entity.RefreshToken;
import com.mockserver.model.entity.User;
import com.mockserver.repository.relational.contract.IRefreshTokenRepo;
import com.mockserver.repository.relational.contract.RelationalBaseRepo;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenRepo extends RelationalBaseRepo<RefreshToken, IRefreshTokenRepo> {
    private final IRefreshTokenRepo iRefreshTokenRepo;
    private static final String ENTITY_NAME = "Refresh Token";

    public RefreshTokenRepo(IRefreshTokenRepo iRefreshTokenRepo) {
        super(iRefreshTokenRepo, ENTITY_NAME);
        this.iRefreshTokenRepo = iRefreshTokenRepo;
    }

    public RefreshToken getByToken(String token) {
        return iRefreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME + " not found"));
    }

    public RefreshToken getByUser(User user) {
        return iRefreshTokenRepo.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME + " not found"));
    }

    public void deleteByUser(User user) {
        iRefreshTokenRepo.deleteByUser(user);
    }
}
