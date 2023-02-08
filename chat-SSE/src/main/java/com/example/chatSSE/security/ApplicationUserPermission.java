package com.example.chatSSE.security;

public enum ApplicationUserPermission {

    USER_REGISTRATION("user:registration"),
    USER_LOGIN("user:login"),
    USER_LOGOUT("user:logOut"),
    USER_SEND("user:send"),
    USER_GETPENDING("user:getPendingMessage"),
    ADMIN_REGISTRATION("admin:registration"),
    ADMIN_LOGIN("admin:login"),
    ADMIN_LOGOUT("admin:logOut"),
    ADMIN_SEND("admin:send"),
    ADMIN_GETPENDING("admin:getPendingMessage"),
    ADMIN_DELETE("admin:deleteById/{id}"),
    ADMIN_GETALLUSERS("admin:getAllUsers");

    private final String permission;


    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}