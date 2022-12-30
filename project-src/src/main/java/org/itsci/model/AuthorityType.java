package org.itsci.attendance.model;

import java.util.ArrayList;
import java.util.List;

public enum AuthorityType {
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_TEACHER("ROLE_TEACHER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private static List<String> authorities = null;
    private final String role;

    AuthorityType(String role) {
        this.role = role;
    }

    public static List<String> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
            for (AuthorityType val : AuthorityType.values()) {
                authorities.add(val.role);
            }
        }
        return authorities;
    }

    @Override
    public String toString() {
        return role;
    }
}
