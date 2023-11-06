package org.devcourse.resumeme.business.user.controller.mentor.dto;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.util.Set;
import java.util.stream.Collectors;

public record MentorInfoResponse(String imageUrl, String nickname, String role, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

    public MentorInfoResponse(Mentor mentor) {
        this(mentor.getImageUrl(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getRole().name(), getExperiencedPositions(mentor), mentor.getIntroduce(), mentor.getCareerYear(), mentor.getIntroduce());
    }

    private static Set<String> getExperiencedPositions(Mentor mentor) {
        return mentor.getExperiencedPositions().stream().map(mentorPosition -> mentorPosition.getPosition().name()).collect(Collectors.toSet());
    }

}
