package org.devcourse.resumeme.global.auth.model.jwt;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;

import java.util.Date;

public record Claims(Long id, String role, Date expiration) {

    public static Claims of(UserCommonInfo info) {
        return new Claims(info.id(), info.role().toString(), new Date());
    }

    public static Claims of(Mentor mentor) {
        return new Claims(mentor.getId(), mentor.getRequiredInfo().getRole().toString(), new Date());
    }

    public static Claims of(Mentee mentee) {
        return new Claims(mentee.getId(), mentee.getRequiredInfo().getRole().toString(), new Date());
    }

}
