package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;

public class CreatedMentorVo implements CreatedUserVo {

    private Mentor mentor;

    public CreatedMentorVo(User user) {
        this.mentor = Mentor.of(user);
    }

    @Override
    public Claims toClaim() {
        return Claims.of(mentor);
    }

}
