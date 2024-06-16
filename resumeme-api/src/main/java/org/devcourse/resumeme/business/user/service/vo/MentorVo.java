package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;

public class MentorVo implements UserDomainVo {

    private Mentor mentor;

    public MentorVo(Mentor mentor) {
        this.mentor = mentor;
    }

    @Override
    public User toUser() {
        return mentor.from();
    }

}
