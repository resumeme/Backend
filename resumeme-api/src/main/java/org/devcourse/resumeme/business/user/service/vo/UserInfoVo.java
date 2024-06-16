package org.devcourse.resumeme.business.user.service.vo;

import lombok.Getter;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

@Getter
public class UserInfoVo {

    private Mentee mentee;

    private Mentor mentor;

    public UserInfoVo(Mentor mentor) {
        this.mentor = mentor;
    }

    public UserInfoVo(Mentee mentee) {
        this.mentee = mentee;
    }

}
