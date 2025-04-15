package io.github.artemnefedov.rstech.config.security;

import static io.github.artemnefedov.rstech.config.security.Authority.CREATE;
import static io.github.artemnefedov.rstech.config.security.Authority.DELETE;
import static io.github.artemnefedov.rstech.config.security.Authority.READ;
import static io.github.artemnefedov.rstech.config.security.Authority.UPDATE;

import java.util.Set;

public enum Role {
    USER(Set.of(READ)),
    ADMIN(Set.of(READ, CREATE, UPDATE, DELETE));

    private final Set<Authority> authorities;

    Role(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }
}
