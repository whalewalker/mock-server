
package com.mockserver.model.response;

import com.mockserver.model.entity.Permission;
import com.mockserver.model.entity.Role;
import com.mockserver.model.entity.User;
import com.mockserver.model.enums.AuthProvider;
import com.mockserver.model.enums.PermissionType;
import com.mockserver.model.enums.RoleType;
import com.mockserver.model.shared.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private AuthProvider provider;
    private Set<RoleType> roles;
    private Set<PermissionType> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        Set<RoleType> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        Set<PermissionType> permissions = user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getName)
            .collect(Collectors.toSet());

        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .provider(user.getProvider())
            .roles(roles)
            .permissions(permissions)
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

public static UserResponse from(UserPrincipal userPrincipal) {
        Set<RoleType> roles = userPrincipal.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());

        Set<PermissionType> permissions = userPrincipal.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(Permission::getName)
            .collect(Collectors.toSet());

        return UserResponse.builder()
            .id(userPrincipal.getId())
            .name(userPrincipal.getName())
            .email(userPrincipal.getEmail())
            .roles(roles)
            .permissions(permissions)
            .build();
    }}