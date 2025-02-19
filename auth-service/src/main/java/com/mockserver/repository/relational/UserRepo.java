package com.mockserver.repository.relational;

import com.mockserver.exception.ResourceNotFoundException;
import com.mockserver.model.entity.User;
import com.mockserver.model.enums.AuthProvider;
import com.mockserver.repository.relational.contract.IUserRepo;
import com.mockserver.repository.relational.contract.RelationalBaseRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepo extends RelationalBaseRepo<User, IUserRepo> {
    private final IUserRepo iUserRepo;
    private static final String ENTITY_NAME = "User";

    protected UserRepo(@Lazy IUserRepo iUserRepo) {
        super(iUserRepo, ENTITY_NAME);
        this.iUserRepo = iUserRepo;
    }

    public User getByEmail(String email) {
        return iUserRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME + " not found"));
    }

    public Optional<User> getByEmailOptional(String email) {
        return iUserRepo.findByEmail(email);
    }

    public User getByProviderAndProviderId(AuthProvider provider, String providerId) {
        return iUserRepo.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NAME + " not found"));
    }

    public boolean existsByEmail(String email) {
        return iUserRepo.existsByEmail(email);
    }
}
