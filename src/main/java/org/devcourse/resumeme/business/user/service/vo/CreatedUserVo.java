package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;
import org.devcourse.resumeme.global.exception.CustomException;

import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;

public interface CreatedUserVo {

    static CreatedUserVo of(User user) {
        Role role = user.getRequiredInfo().getRole();

        return switch (role) {
            case ROLE_PENDING -> new CreatedMentorVo(user);
            case ROLE_MENTEE -> new CreatedMenteeVo(user);
            case ROLE_MENTOR, ROLE_ADMIN -> throw new CustomException(BAD_REQUEST);
        };
    }

    Claims toClaim();

}
