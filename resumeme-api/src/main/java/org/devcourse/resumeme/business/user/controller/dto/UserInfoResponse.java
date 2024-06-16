package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.vo.UserInfoVo;

import java.util.Set;
import java.util.stream.Collectors;

public record UserInfoResponse(Long id, String imageUrl, String realName, String nickname, String phoneNumber, String role,
                               Set<String> experiencedPositions, Set<String> interestedPositions,
                               Set<String> interestedFields, String careerContent, int careerYear, String introduce) {

    public static UserInfoResponse of(UserInfoVo userInfoVo) {
        if (userInfoVo.getMentee() == null) {
            return new UserInfoResponse(userInfoVo.getMentor());
        }

        return new UserInfoResponse(userInfoVo.getMentee());
    }

    private UserInfoResponse(Mentor mentor) {
        this(mentor.getId(), mentor.getImageUrl(), mentor.getRequiredInfo().getRealName(), mentor.getRequiredInfo().getNickname(), mentor.getRequiredInfo().getPhoneNumber(), mentor.getRoleName(), getExperiencedPositions(mentor), null, null, mentor.getCareerContent(), mentor.getCareerYear(), mentor.getIntroduce());
    }

    private UserInfoResponse(Mentee mentee) {
        this(mentee.getId(), mentee.getImageUrl(), mentee.getRequiredInfo().getRealName(), mentee.getRequiredInfo().getNickname(), mentee.getRequiredInfo().getPhoneNumber(), mentee.getRoleName(), null, getInterestedPositions(mentee), getInterestedFields(mentee), null, 0, mentee.getIntroduce());
    }

    private static Set<String> getExperiencedPositions(Mentor mentor) {
        return mentor.getExperiencedPositions().stream().map(Enum::name).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedPositions(Mentee mentee) {
        return mentee.getInterestedPositions().stream().map(Enum::name).collect(Collectors.toSet());
    }

    private static Set<String> getInterestedFields(Mentee mentee) {
        return mentee.getInterestedFields().stream().map(Enum::name).collect(Collectors.toSet());
    }

}
