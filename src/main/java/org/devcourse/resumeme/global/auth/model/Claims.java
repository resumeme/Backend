package org.devcourse.resumeme.global.auth.model;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;

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
