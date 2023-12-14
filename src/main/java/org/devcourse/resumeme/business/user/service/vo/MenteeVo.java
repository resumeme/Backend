package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;

public class MenteeVo implements UserDomainVo {

    private Mentee mentee;

    public MenteeVo(Mentee mentee) {
        this.mentee = mentee;
    }

    @Override
    public User toUser() {
        return mentee.from();
    }

}
