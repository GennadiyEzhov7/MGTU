package com.mvarlamov.Music.data.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER, ANON;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
