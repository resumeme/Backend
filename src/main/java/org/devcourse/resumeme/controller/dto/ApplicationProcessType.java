package org.devcourse.resumeme.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.domain.user.Role;

import static org.devcourse.resumeme.domain.user.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.domain.user.Role.ROLE_PENDING;

public enum ApplicationProcessType implements DocsEnumType {

    ACCEPT("승인", ROLE_MENTOR),
    REJECT("거절", ROLE_PENDING);

    @Getter
    private final Role role;

    private final String description;

    ApplicationProcessType(String description, Role role) {
        this.description = description;
        this.role = role;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

}
