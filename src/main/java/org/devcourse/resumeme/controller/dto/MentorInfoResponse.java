package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentor.Mentor;

public record MentorInfoResponse(String imageUrl, String nickname, String careerContent, int careerYear, String introduce) {

    public static MentorInfoResponse of(Mentor mentor) {
        return new MentorInfoResponse(mentor.getImageUrl(), mentor.getRequiredInfo().getNickname(), mentor.getCareerContent(), mentor.getCareerYear(), mentor.getIntroduce());
    }

}
