package ru.alwertus.alwserv.auth;

public enum Permission {
    ADMIN_FLAG("admin"),
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write");

    private final String permission;
    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
