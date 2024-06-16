package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.user.domain.Role;

public enum AuthorizationRole {
    MENTOR,
    MENTEE,
    ALL;

    public static AuthorizationRole of(Role role) {
        return switch (role) {
            case ROLE_MENTEE -> MENTEE;
            case ROLE_PENDING, ROLE_MENTOR -> MENTOR;
            case ROLE_ADMIN -> ALL;
        };
    }
}
