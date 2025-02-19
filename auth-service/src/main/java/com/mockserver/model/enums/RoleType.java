package com.mockserver.model.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SERVICE("ROLE_SERVICE");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }
}