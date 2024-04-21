package com.mvarlamov.lab10.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    ANONYMOUS,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
