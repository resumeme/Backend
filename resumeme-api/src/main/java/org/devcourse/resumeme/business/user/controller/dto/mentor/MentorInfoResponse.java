package org.devcourse.resumeme.business.user.controller.dto.mentor;

import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.util.Set;
import java.util.stream.Collectors;

public record MentorInfoResponse(Long id, String imageUrl, String nickname, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

    public MentorInfoResponse(Mentor mentor) {
        this(mentor.getId(), mentor.getImageUrl(), mentor.getRequiredInfo().getNickname(), getExperiencedPositions(mentor), mentor.getCareerContent(), mentor.getCareerYear(), mentor.getIntroduce());
    }

    private static Set<String> getExperiencedPositions(Mentor mentor) {
        return mentor.getExperiencedPositions().stream().map(Enum::name).collect(Collectors.toSet());
    }

}
