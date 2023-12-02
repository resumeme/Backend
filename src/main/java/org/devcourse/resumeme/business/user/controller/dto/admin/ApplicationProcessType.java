package org.devcourse.resumeme.business.user.controller.dto.admin;

import lombok.Getter;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum ApplicationProcessType implements DocsEnumType {

    ACCEPT("승인", Role.ROLE_MENTOR),
    REJECT("거절", Role.ROLE_PENDING);

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
