package com.itrex.java.lab.entity;

public enum Permission {
    CONTRACT_READ("contract:read"),
    CONTRACT_CRUD("contract:crud"),
    OFFER_READ("offer:read"),
    OFFER_CRUD("offer:crud"),
    USER_READ("user:read"),
    USER_CRUD("user:crud"),
    REPORT_READ("report:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
