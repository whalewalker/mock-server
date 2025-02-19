package com.mockserver.repository.relational;

import com.mockserver.exception.ResourceNotFoundException;
import com.mockserver.model.entity.Role;
import com.mockserver.model.enums.RoleType;
import com.mockserver.repository.relational.contract.IRoleRepo;
import com.mockserver.repository.relational.contract.RelationalBaseRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class RoleRepo extends RelationalBaseRepo<Role, IRoleRepo> {
    private final IRoleRepo iRoleRepo;
    private static final String ENTITY_NAME = "Role";

    protected RoleRepo(@Lazy IRoleRepo iRoleRepo) {
        super(iRoleRepo, ENTITY_NAME);
        this.iRoleRepo = iRoleRepo;
    }

    public Set<Role> getByNameIn(RoleType... roleNames) {
        return iRoleRepo.findByNameIn(Arrays.asList(roleNames));
    }
}
