package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.global.auth.model.jwt.Claims;

public class CreatedMenteeVo implements CreatedUserVo {

    private Mentee mentee;

    public CreatedMenteeVo(User mentee) {
        this.mentee = Mentee.of(mentee);
    }

    @Override
    public Claims toClaim() {
        return Claims.of(mentee);
    }

}
