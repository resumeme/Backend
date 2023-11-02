package org.devcourse.resumeme.global.auth.model;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.user.Role;

public record UserCommonInfo(Long id, String email, Role role) {

    public static UserCommonInfo of(Mentor mentor) {
        return new UserCommonInfo(mentor.getId(), mentor.getEmail(), mentor.getRequiredInfo().getRole());
    }

    public static UserCommonInfo of(Mentee mentee) {
        return new UserCommonInfo(mentee.getId(), mentee.getEmail(), mentee.getRequiredInfo().getRole());
    }

}
