package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.controller.dto.mentor.MentorInfoResponse;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

public record FollowResponse(Long followId, MentorInfoResponse mentorInfo) {

    public FollowResponse(Follow follow, Mentor mentor) {
        this(follow.getId(), new MentorInfoResponse(mentor));
    }

}
