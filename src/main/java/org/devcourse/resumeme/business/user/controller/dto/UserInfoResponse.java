package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.util.Set;
import java.util.stream.Collectors;

public record UserInfoResponse(String imageUrl, String realName, String nickname, String phoneNumber, String role,
                               Set<String> experiencedPositions, Set<String> interestedPositions,
                               Set<String> interestedFields, String careerContent, int careerYear, String introduce) {

    public UserInfoResponse(Mentor mentor) {
        this(mentor.getImageUrl(), mentor.getRequiredInfo().getRealName(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getPhoneNumber(), mentor.getRequiredInfo().getRole().equals(Role.ROLE_MENTOR) ? "mentor" : "pending", getExperiencedPositions(mentor), null, null, mentor.getCareerContent(), mentor.getCareerYear(), mentor.getIntroduce());
    }

    public UserInfoResponse(Mentee mentee) {
        this(mentee.getImageUrl(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getNickname(), mentee.getRequiredInfo().getPhoneNumber(), "mentee", null, getInterestedPositions(mentee), getInterestedFields(mentee), null, 0, mentee.getIntroduce());
    }

    private static Set<String> getExperiencedPositions(Mentor mentor) {
        return mentor.getExperiencedPositions().stream().map(mentorPosition -> mentorPosition.getPosition().name()).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedPositions(Mentee mentee) {
        return mentee.getInterestedPositions().stream().map(menteePosition -> menteePosition.getPosition().name()).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedFields(Mentee mentee) {
        return mentee.getInterestedFields().stream().map(menteeField -> menteeField.getField().name()).collect(Collectors.toSet());
    }

}
