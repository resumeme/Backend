package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.domain.mentee.Follow;

public record FollowResponse(Long followId, Long mentorId) {

    public FollowResponse(Follow follow) {
        this(follow.getId(), follow.getMentorId());
    }

}
