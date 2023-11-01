package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentor.Mentor;

public record MentorInfoResponse(String imageUrl, String nickname, String role, String careerContent, int careerYear, String introduce) {

    public MentorInfoResponse(Mentor mentor) {
        this(mentor.getImageUrl(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getRole().toString(), mentor.getIntroduce(), mentor.getCareerYear(), mentor.getIntroduce());
    }

}
