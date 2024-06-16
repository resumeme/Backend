package org.devcourse.resumeme.global.auth.service.authorization;

public enum AuthorizationTarget {
    EVENTS,
    RESUMES,
    MENTEES,
    MENTORS;

    public static AuthorizationTarget of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
