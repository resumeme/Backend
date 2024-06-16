package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.controller.dto.mentee.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentor.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.service.vo.UpdateUserVo;

public record UserInfoUpdateRequest(
        MentorInfoUpdateRequest mentors,
        MenteeInfoUpdateRequest mentees
) {

    public UpdateUserVo toVo() {
        if (mentors == null) {
            return mentees.toVo();
        }

        return mentors.toVo();
    }

}
