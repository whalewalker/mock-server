package com.mockserver.repository.relational.contract;

import com.mockserver.model.entity.Role;
import com.mockserver.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IRoleRepo extends JpaRepository<Role, UUID> {
    Set<Role> findByNameIn(List<RoleType> roleNames);
}
