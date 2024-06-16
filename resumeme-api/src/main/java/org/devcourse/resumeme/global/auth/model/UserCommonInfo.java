package org.devcourse.resumeme.global.auth.model;

import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.entity.User;

public record UserCommonInfo(Long id, String email, Role role) {

    public static UserCommonInfo of(User user) {
        return new UserCommonInfo(user.getId(), user.getEmail(), user.getRequiredInfo().getRole());
    }

}
