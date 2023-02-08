package com.example.chatSSE.security;


import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_DELETE;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_GETALLUSERS;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_GETPENDING;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_LOGIN;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_LOGOUT;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_REGISTRATION;
import static com.example.chatSSE.security.ApplicationUserPermission.ADMIN_SEND;
import static com.example.chatSSE.security.ApplicationUserPermission.USER_GETPENDING;
import static com.example.chatSSE.security.ApplicationUserPermission.USER_LOGIN;
import static com.example.chatSSE.security.ApplicationUserPermission.USER_LOGOUT;
import static com.example.chatSSE.security.ApplicationUserPermission.USER_REGISTRATION;
import static com.example.chatSSE.security.ApplicationUserPermission.USER_SEND;

import com.google.common.collect.Sets;
import java.util.Set;

public enum ApplicationUserRole {
    USER(Sets.newHashSet(USER_REGISTRATION, USER_LOGIN, USER_LOGOUT, USER_SEND, USER_GETPENDING)),
    ADMIN(Sets.newHashSet(ADMIN_REGISTRATION, ADMIN_LOGIN, ADMIN_LOGOUT, ADMIN_SEND, ADMIN_GETPENDING, ADMIN_DELETE, ADMIN_GETALLUSERS));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }
}
