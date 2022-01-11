package com.itrex.java.lab.entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {

    OWNER(Set.of()),
    ADMIN(Set.of(Permission.USER_READ, Permission.USER_CRUD, Permission.CONTRACT_READ, Permission.OFFER_READ,
            Permission.REPORT_READ, Permission.DATABASE_FILL)),
    CUSTOMER(Set.of(Permission.USER_READ, Permission.CONTRACT_READ, Permission.CONTRACT_CRUD, Permission.OFFER_READ)),
    CONTRACTOR(Set.of(Permission.USER_READ, Permission.CONTRACT_READ, Permission.OFFER_READ, Permission.OFFER_CRUD));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
    }
}
